package com.example.service;


import com.example.annotation.KoListener;
import com.example.handler.InvokedHandler;
import com.example.model.MethodNode;
import com.example.util.Context;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;

import java.lang.reflect.Parameter;
import java.util.logging.Logger;

@KoListener
@Lazy
public class EmailHandler implements InvokedHandler {
    private static Logger log = Logger.getLogger(EmailHandler.class.toString());
    @Value("${ko-time.mail-scope:Controller}")
    private String mailScope;

    @Override
    public void onInvoked(MethodNode current, MethodNode parent, Parameter[] names, Object[] values) {
        if (!Context.getConfig().getMailEnable()) {
            return;
        }
        if (current == null || current.getValue() < Context.getConfig().getThreshold()) {
            return;
        }
        if (mailScope.equals("All") || current.getMethodType().name().equals(mailScope)) {
            EmailSendService.sendNoticeAsync(current);
        }
    }
}
