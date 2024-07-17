package com.damai.captcha.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: DateUtils
 * @author: 阿星不是程序员
 **/
public class DateUtils {

    private static final Logger logger = LoggerFactory.getLogger(CacheUtil.class);

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DAYTIME_START = "00:00:00";
    public static final String DAYTIME_END = "23:59:59";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DC_DATE_FORMAT = "yyyy/MM/dd";
    public static final String DC_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
    public static final Integer TEN = 10;
    public static final char T = 'T';
    /**
     * 获取两个日期之间的所有月份 (年月)
     *
     * @param startTime
     * @param endTime
     * @return：YYYY-MM
     */
    public static List<String> getMonthBetweenDate(String startTime, String endTime,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // 声明保存日期集合
        List<String> list = new ArrayList<>();
        try {
            // 转化成日期类型
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);

            //用Calendar 进行日期比较判断
            Calendar calendar = Calendar.getInstance();
            while (startDate.getTime()<=endDate.getTime()){
                // 把日期添加到集合
                list.add(sdf.format(startDate));
                // 设置日期
                calendar.setTime(startDate);
                //把日期增加一天
                calendar.add(Calendar.MONTH, 1);
                // 获取增加后的日期
                startDate=calendar.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static int differentDays(Date startDate, Date endDate) {
        Calendar sCalendar = Calendar.getInstance();
        sCalendar.setTime(startDate);
        sCalendar.set(Calendar.HOUR_OF_DAY, 0);
        sCalendar.set(Calendar.MINUTE, 0);
        sCalendar.set(Calendar.SECOND, 0);
        sCalendar.set(Calendar.MILLISECOND, 0);
        Calendar eCalendar = Calendar.getInstance();
        eCalendar.setTime(endDate);
        eCalendar.set(Calendar.HOUR_OF_DAY, 0);
        eCalendar.set(Calendar.MINUTE, 0);
        eCalendar.set(Calendar.SECOND, 0);
        eCalendar.set(Calendar.MILLISECOND, 0);
        System.out.println(eCalendar.getTimeInMillis());
        System.out.println(sCalendar.getTimeInMillis());
        int days = (int)((eCalendar.getTimeInMillis() - sCalendar.getTimeInMillis())/(1000*3600*24));
        return days;
    }

    public static Date afterDays(Date startDate, Integer days) {
        Calendar sCalendar = Calendar.getInstance();
        sCalendar.setTime(startDate);
        sCalendar.add(Calendar.DATE, days);
        return sCalendar.getTime();
    }

    public static String convert(Date date, String dateFormat) {
        if (date == null) {
            return null;
        }

        if (null == dateFormat) {
            dateFormat = DATE_TIME_FORMAT;
        }

        return new SimpleDateFormat(dateFormat).format(date);
    }

    private static final String[] FORMATS = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd HH:mm:ss", "HH:mm", "HH:mm:ss", "yyyy-MM",
            "yyyy-MM-dd HH:mm:ss.S",DC_DATE_FORMAT,DC_TIME_FORMAT };

    public static Date convert(String str) {
        if (str != null && str.length() > 0) {
            if (str.length() > TEN && str.charAt(TEN) == T) {
                // 去掉json-lib加的T字母
                str = str.replace(T, ' '); 
            }
            for (String format : FORMATS) {
                if (str.length() == format.length()) {
                    try {
                        return new SimpleDateFormat(format).parse(str);
                    } catch (ParseException e) {
                        if (logger.isWarnEnabled()) {
                            logger.warn(e.getMessage());
                        }
                    }
                }
            }
        }
        return null;
    }
    /**
     * 返回该天从00:00:00开始的日期
     *
     * @param date
     * @return
     */
    public static Date getStartDatetime(Date date) {
        String thisDate = convert(date, DATE_FORMAT);
        return convert(thisDate + " " + DAYTIME_START);

    }

    /**
     * 返回该天到23:59:59结束的日期
     *
     * @param date
     * @return
     */
    public static Date getEndDatetime(Date date) {
        String thisDate = convert(date, DATE_FORMAT);
        return convert(thisDate + " " + DAYTIME_END);

    }

}
