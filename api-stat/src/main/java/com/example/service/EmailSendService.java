package com.example.service;


import com.example.model.MethodNode;
import com.example.util.Context;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class EmailSendService{
    private static Logger log = Logger.getLogger(EmailSendService.class.toString());
    private volatile static JavaMailSenderImpl javaMailSender;

    private static ConcurrentMap<String, Integer> redMethods = new ConcurrentHashMap<>();
    private final static ExecutorService emailExecutors = Executors.newFixedThreadPool(5);

    public static JavaMailSender getMailSender() {
        if (javaMailSender == null) {
            synchronized (EmailSendService.class) {
                if (javaMailSender == null) {
                    javaMailSender = new JavaMailSenderImpl();
                    javaMailSender.setHost(Context.getConfig().getMailHost());
                    javaMailSender.setPort(Context.getConfig().getMailPort());
                    javaMailSender.setUsername(Context.getConfig().getMailUser());
                    javaMailSender.setPassword(Context.getConfig().getMailCode());
                    javaMailSender.setProtocol(Context.getConfig().getMailProtocol());
                    javaMailSender.setDefaultEncoding(Context.getConfig().getMailEncoding());
                }
            }
        }
        return javaMailSender;
    }

    public static void sendNoticeAsync(MethodNode current) {
        emailExecutors.submit(() -> sendNotice(current));
    }


    public static void sendNotice(MethodNode current) {
        if (!StringUtils.hasText(Context.getConfig().getMailReceivers())) {
            return;
        }
        JavaMailSender mailSender = getMailSender();
        if (redMethods.containsKey(current.getId())) {
            int n = redMethods.get(current.getId());
            n += 1;
            if (n >= Context.getConfig().getMailThreshold()) {
                mailSender.send(createMessage(mailSender,current));
                redMethods.put(current.getId(), -2000);
            } else {
                redMethods.put(current.getId(), n);
            }
        } else {
            redMethods.put(current.getId(), 1);
            if (Context.getConfig().getMailThreshold() == 1) {
                mailSender.send(createMessage(mailSender,current));
            }
        }
    }

    private static MimeMessage createMessage(JavaMailSender mailSender, MethodNode current) {
        MimeMessage mimeMessage = null;
        try {
            mimeMessage = mailSender.createMimeMessage();
            configMessage(current, mimeMessage);
        } catch (MessagingException e) {
            log.severe("Error email message!");
        } catch (javax.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
        return mimeMessage;
    }

    private static void configMessage(MethodNode current, MimeMessage mimeMessage) throws MessagingException, javax.mail.MessagingException {
        String[] receiversArray = Context.getConfig().getMailReceivers().split(",");
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setSubject("KoTime耗时预警-" + Context.getConfig().getDataPrefix() + "-" + current.getName());
        messageHelper.setFrom(Context.getConfig().getMailUser());
        messageHelper.setTo(receiversArray);
        messageHelper.setSentDate(new Date());
        messageHelper.setText(createNoticeTemplate(current), true);
    }

    private static String createNoticeTemplate(MethodNode current) {
        return "<h4>您有一个方法耗时有" + Context.getConfig().getMailThreshold() + "次超过了阈值（" + Context.getConfig().getThreshold() + "ms），详情如下：</h4>\n" +
                "<div style=\"background-color: #fafdfd;border-radius: 5px;width: 600px;padding: 10px;box-shadow: #75f1bf 2px 2px 2px 2px\">\n" +
                "    <div>项目：" + Context.getConfig().getDataPrefix() + "</div>\n" +
                "    <div>类名：" + current.getClassName() + "</div>\n" +
                "    <div>方法：" + current.getMethodName() + "</div>\n" +
                "    <div>最近耗时：" + current.getValue() + " ms</div>\n" +
                "</div>\n" +
                "<p>请前往系统查看</p>\n" +
                "<p>" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "</p>";
    }
}
