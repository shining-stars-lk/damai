package com.damai.util;

import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 日期工具
 * @author: 阿星不是程序员
 **/
public class DateUtils {
 
    /** 一星期的天数 */
    public static final int WEEK_DAYS = 7;
    /** 一年的月份数 */
    public static final int YEAR_MONTHS = 12;
    /** 一天的小时数 */
    public static final int DAY_HOURS = 24;
    /** 一小时分钟数 */
    public static final int HOUR_MINUTES = 60;
    /** 一天分钟数 (24 * 60) */
    public static final int DAY_MINUTES = 1440;
    /** 一分钟的秒数 */
    public static final int MINUTE_SECONDS = 60;
    /** 一个小时的秒数 (60 * 60) */
    public static final int HOUR_SECONDS = 3600;
    /** 一天的秒数 (24 * 60 * 60) */
    public static final int DAY_SECONDS = 86400;
    /** 一秒的毫秒数 */
    public static final long SECOND_MILLISECONDS = 1000L;
    /** 一分钟的毫秒数（60 * 1000） */
    public static final long MINUTE_MILLISECONDS = 60000L;
    /** 一小时的毫秒数（60 * 60 * 1000） */
    public static final long HOUR_MILLISECONDS = 3600000L;
    /** 一天的毫秒数（24 * 60* 60* 1000） */
    public static final long DAY_MILLISECONDS = 86400000L;
    /** 星期一 */
    public static final int WEEK_1_MONDAY = 1;
    /** 星期二 */
    public static final int WEEK_2_TUESDAY = 2;
    /** 星期三 */
    public static final int WEEK_3_WEDNESDAY = 3;
    /** 星期四 */
    public static final int WEEK_4_THURSDAY = 4;
    /** 星期五 */
    public static final int WEEK_5_FRIDAY = 5;
    /** 星期六 */
    public static final int WEEK_6_SATURDAY = 6;
    /** 星期天 */
    public static final int WEEK_7_SUNDAY = 7;
    /** 一月 */
    public static final int MONTH_1_JANUARY = 1;
    /** 二月 */
    public static final int MONTH_2_FEBRUARY = 2;
    /** 三月 */
    public static final int MONTH_3_MARCH = 3;
    /** 四月 */
    public static final int MONTH_4_APRIL= 4;
    /** 五月 */
    public static final int MONTH_5_MAY = 5;
    /** 六月 */
    public static final int MONTH_6_JUNE = 6;
    /** 七月 */
    public static final int MONTH_7_JULY = 7;
    /** 八月 */
    public static final int MONTH_8_AUGUST = 8;
    /** 九月 */
    public static final int MONTH_9_SEPTEMBER = 9;
    /** 十月 */
    public static final int MONTH_10_OCTOBER = 10;
    /** 十一月 */
    public static final int MONTH_11_NOVEMBER = 11;
    /** 十二月 */
    public static final int MONTH_12_DECEMBER= 12;
    /** 显示到日期 */
    public static final String FORMAT_DATE = "yyyy-MM-dd";
    /** 显示到小时 */
    public static final String FORMAT_HOUR = "yyyy-MM-dd HH";
    /** 显示到分 */
    public static final String FORMAT_MINUTE = "yyyy-MM-dd HH:mm";
    /** 显示到秒 */
    public static final String FORMAT_SECOND = "yyyy-MM-dd HH:mm:ss";
    /** 显示到毫秒 */
    public static final String FORMAT_MILLISECOND = "yyyy-MM-dd HH:mm:ss:SSS";
    /** 显示到日期（数字格式） */
    public static final String FORMAT_NO_DATE = "yyyyMMdd";
    /** 显示到小时（数字格式） */
    public static final String FORMAT_NO_HOUR = "yyyyMMddHH";
    /** 显示到分（数字格式） */
    public static final String FORMAT_NO_MINUTE = "yyyyMMddHHmm";
    /** 显示到秒（数字格式） */
    public static final String FORMAT_NO_SECOND = "yyyyMMddHHmmss";
    /** 显示到毫秒（数字格式） */
    public static final String FORMAT_NO_MILLISECOND = "yyyyMMddHHmmssSSS";
    
    public static final String FORMAT_UTC = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    
    /**
     * 获取北京当前时间
     * */
    public static Date now(){
        return parseDateTime(getFormatedDateString(8,FORMAT_SECOND));
    }
    
    /**
     * 获取北京当前时间
     * */
    public static Date now(String format){
        return parseDateTime(getFormatedDateString(8,format),format);
    }
    
    public static String nowStr(){
        return getFormatedDateString(8, FORMAT_SECOND);
    }
    
    public static String nowStr(String pattern){
        return getFormatedDateString(8, pattern);
    }
 
    /**
     * 获取指定时间格式化器
     *
     * @param formatStyle 时间格式
     * @return 时间格式化器
     */
    private static SimpleDateFormat getSimpleDateFormat(String formatStyle) {
        return new SimpleDateFormat(formatStyle);
    }
 
    /**
     * 将 Date 格式时间转化为指定格式时间
     *
     * @param date        Date 格式时间
     * @param formatStyle 转化指定格式（如: yyyy-MM-dd HH:mm:ss）
     * @return 转化格式时间
     */
    public static String format(Date date, String formatStyle) {
        if (Objects.isNull(date)) {
            return "";
        }
        return getSimpleDateFormat(formatStyle).format(date);
    }
 
    /**
     * 将 Date 格式时间转化为 yyyy-MM-dd 格式时间
     *
     * @param date Date 格式时间
     * @return yyyy-MM-dd 格式时间（如：2022-06-17）
     */
    public static String formatDate(Date date) {
        return format(date, FORMAT_DATE);
    }
 
    /**
     * 将 Date 格式时间转化为 yyyy-MM-dd HH:mm:ss 格式时间
     *
     * @param date Date 格式时间
     * @return yyyy-MM-dd HH:mm:ss 格式时间（如：2022-06-17 16:06:17）
     */
    public static String formatDateTime(Date date) {
        return format(date, FORMAT_SECOND);
    }
 
    /**
     * 将 Date 格式时间转化为 yyyy-MM-dd HH:mm:ss:SSS 格式时间
     *
     * @param date Date 格式时间
     * @return yyyy-MM-dd HH:mm:ss:SSS 格式时间（如：2022-06-17 16:06:17:325）
     */
    public static String formatDateTimeStamp(Date date) {
        return format(date, FORMAT_MILLISECOND);
    }
    
    /**
     * 将 Date 格式时间转化为 yyyy-MM-dd HH:mm:ss 格式时间
     *
     * @param date Date 格式时间
     * @return yyyy-MM-dd HH:mm:ss 格式时间（如：2022-06-17 16:06:17）
     */
    public static String formatUtcTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_UTC);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return sdf.format(date);
    }
    
    /**
     * 将 yyyy-MM-dd 格式时间转化为 Date 格式时间
     * 
     * @param dateString yyyy-MM-dd 格式时间（如：2022-06-17）
     * @return Date 格式时间
     */
    public static Date parseDate(String dateString) {
        return parse(dateString, FORMAT_DATE);
    }
 
    /**
     * 将 yyyy-MM-dd HH:mm:ss 格式时间转化为 Date 格式时间
     * 
     * @param dateTimeStr yyyy-MM-dd HH:mm:ss 格式时间（如：2022-06-17 16:06:17）
     * @return Date 格式时间
     */
    public static Date parseDateTime(String dateTimeStr) {
        return parse(dateTimeStr, FORMAT_SECOND);
    }
    
    /**
     * 按照format 格式时间转化为 Date 格式时间
     *
     * @param dateTimeStr yyyy-MM-dd HH:mm:ss 格式时间（如：2022-06-17 16:06:17）
     * @param format 格式
     * @return Date 格式时间
     */
    public static Date parseDateTime(String dateTimeStr,String format) {
        return parse(dateTimeStr, format);
    }
 
    /**
     * 将 yyyy-MM-dd HH:mm:ss:SSS 格式时间转化为 Date 格式时间
     * 
     * @param dateTimeStampStr yyyy-MM-dd HH:mm:ss:SSS 格式时间（如：2022-06-17 16:06:17）
     * @return Date 格式时间
     */
    public static Date parseDateTimeStamp(String dateTimeStampStr) {
        return parse(dateTimeStampStr, FORMAT_MILLISECOND);
    }
    
    /**
     * 将时间戳转化为日期
     *
     * @param timestamp
     * @return
     */
    public static Date parse(Long timestamp) {
        return new Date(timestamp);
    }
 
    /**
     * 将字符串格式时间转化为 Date 格式时间
     * 
     * @param dateString 字符串时间（如：2022-06-17 16:06:17）
     * @return formatStyle 格式内容
     * @return Date 格式时间
     */
    public static Date parse(String dateString, String formatStyle) {
        String s = getString(dateString);
        if (s.isEmpty()) {
            return null;
        }
        try {
            return getSimpleDateFormat(formatStyle).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
 
    /**
     * 获取字符串有效内容
     * 
     * @param s 字符串
     * @return 有效内容
     */
    private static String getString(String s) {
        return Objects.isNull(s) ? "" : s.trim();
    }
 
    /**
     * 获取一天的开始时间（即：0 点 0 分 0 秒 0 毫秒）
     * 
     * @param date 指定时间
     * @return 当天的开始时间
     */
    public static Date getDateStart(Date date) {
        if (Objects.isNull(date)) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
 
    /**
     * 获取一天的截止时间（即：23 点 59 分 59 秒 999 毫秒）
     * 
     * @param date 指定时间
     * @return 当天的开始时间
     */
    public static Date getDateEnd(Date date) {
        if (Objects.isNull(date)) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
 
    /**
     * 获取日期数字
     * 
     * @param date 日期
     * @return 日期数字
     */
    public static int getDateNo(Date date) {
        if (Objects.isNull(date)) {
            return 0;
        }
        return Integer.valueOf(format(date, FORMAT_NO_DATE));
    }
 
    /**
     * 获取日期时间数字（到秒）
     * 
     * @param date 日期
     * @return 日期数字
     */
    public static long getDateTimeNo(Date date) {
        if (Objects.isNull(date)) {
            return 0L;
        }
        return Long.parseLong(format(date, FORMAT_NO_SECOND));
    }
 
    /**
     * 获取日期时间数字（到毫秒）
     * 
     * @param date 日期
     * @return 日期数字
     */
    public static long getDateTimeStampNo(Date date) {
        if (Objects.isNull(date)) {
            return 0L;
        }
        return Long.parseLong(format(date, FORMAT_NO_MILLISECOND));
    }
 
    /**
     * 获取星期几
     * 
     * @param date 时间
     * @return 0（时间为空）， 1（周一）， 2（周二），3（周三），4（周四），5（周五），6（周六），7（周日）
     */
    public static int getWeek(Date date) {
        if (Objects.isNull(date)) {
            return 0;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getWeek(calendar);
    }
    
    /**
     * 获取星期几
     *
     * @param date 时间
     * @return 0（时间为空）， 1（周一）， 2（周二），3（周三），4（周四），5（周五），6（周六），7（周日）
     */
    public static String getWeekStr(Date date) {
        if (Objects.isNull(date)) {
            return "未知";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getWeekStr(calendar);
    }
 
    /**
     * 获取星期几
     * 
     * @param calendar 时间
     * @return 0（时间为空）， 1（周一）， 2（周二），3（周三），4（周四），5（周五），6（周六），7（周日）
     */
    private static int getWeek(Calendar calendar) {
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
        case Calendar.MONDAY:
            return 1;
        case Calendar.TUESDAY:
            return 2;
        case Calendar.WEDNESDAY:
            return 3;
        case Calendar.THURSDAY:
            return 4;
        case Calendar.FRIDAY:
            return 5;
        case Calendar.SATURDAY:
            return 6;
        case Calendar.SUNDAY:
            return 7;
        default:
            return 0;
        }
    }
    
    /**
     * 获取星期几
     *
     * @param calendar 时间
     * @return 星期
     */
    private static String getWeekStr(Calendar calendar) {
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return "周一";
            case Calendar.TUESDAY:
                return "周二";
            case Calendar.WEDNESDAY:
                return "周三";
            case Calendar.THURSDAY:
                return "周四";
            case Calendar.FRIDAY:
                return "周五";
            case Calendar.SATURDAY:
                return "周六";
            case Calendar.SUNDAY:
                return "周日";
            default:
                return "未知";
        }
    }
 
    /**
     * 获取该日期是今年的第几周（以本年的周一为第1周，详见下面说明）<br>
     * 
     * 【说明】<br>
     * 比如 2022-01-01（周六）和 2022-01-02（周日）虽然在 2022 年里，但他们两天则属于 2021 年最后一周，<br>
     * 那么这两天不会算在 2022 年第 1 周里，此时会返回 0 ；而 2022 年第 1 周将从 2022-01-03（周一） 开始计算。<br>
     * 
     * @param date 时间
     * @return -1（时间为空）， 0（为上个年的最后一周），其他数字（今年的第几周）
     */
    public static int getWeekOfYear(Date date) {
        if (Objects.isNull(date)) {
            return -1;
        }
        int weeks = getWeekOfYearIgnoreLastYear(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int week = getWeek(calendar);
        if (week == 1) {
            return weeks;
        }
        return weeks - 1;
    }
 
    /**
     * 获取今年的第几周（以本年的1月1日为第1周第1天）<br>
     * 
     * @param date 时间
     * @return -1（时间为空），其他数字（今年的第几周）
     */
    public static int getWeekOfYearIgnoreLastYear(Date date) {
        int seven = 7;
        if (Objects.isNull(date)) {
            return -1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int days = calendar.get(Calendar.DAY_OF_YEAR);
        int weeks = days / seven;
        // 如果是 7 的倍数，则表示恰好是多少周
        if (days % seven == 0) {
            return weeks;
        }
        // 如果有余数，则需要再加 1
        return weeks + 1;
    }
 
    /**
     * 获取时间节点对象
     * 
     * @param date 时间对象
     * @return DateNode
     */
    public static DateNode getDateNode(Date date) {
        if (Objects.isNull(date)) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        DateNode node = new DateNode();
        node.setTime(format(date, FORMAT_MILLISECOND));
        node.setYear(calendar.get(Calendar.YEAR));
        node.setMonth(calendar.get(Calendar.MONTH) + 1);
        node.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        node.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        node.setMinute(calendar.get(Calendar.MINUTE));
        node.setSecond(calendar.get(Calendar.SECOND));
        node.setMillisecond(calendar.get(Calendar.MILLISECOND));
        node.setWeek(getWeek(calendar));
        node.setDayOfYear(calendar.get(Calendar.DAY_OF_YEAR));
        node.setWeekOfYear(getWeekOfYear(date));
        node.setWeekOfYearIgnoreLastYear(getWeekOfYearIgnoreLastYear(date));
        node.setMillisecondStamp(date.getTime());
        node.setSecondStamp(node.getMillisecondStamp() / 1000);
        return node;
    }
 
    /**
     * 日期变更
     * 
     * @param date   指定日期
     * @param field  变更属性（如变更年份，则该值为 Calendar.DAY_OF_YEAR）
     * @param amount 变更大小（大于 0 时增加，小于 0 时减少）
     * @return 变更后的日期时间
     */
    public static Date add(Date date, int field, int amount) {
        if (Objects.isNull(date)) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }
 
    /**
     * 指定日期加减年份
     * 
     * @param date 指定日期
     * @param year 变更年份（大于 0 时增加，小于 0 时减少）
     * @return 变更年份后的日期
     */
    public static Date addYear(Date date, int year) {
        return add(date, Calendar.YEAR, year);
    }
 
    /**
     * 指定日期加减月份
     * 
     * @param date  指定日期
     * @param month 变更月份（大于 0 时增加，小于 0 时减少）
     * @return 变更月份后的日期
     */
    public static Date addMonth(Date date, int month) {
        return add(date, Calendar.MONTH, month);
    }
 
    /**
     * 指定日期加减天数
     * 
     * @param date 指定日期
     * @param day  变更天数（大于 0 时增加，小于 0 时减少）
     * @return 变更天数后的日期
     */
    public static Date addDay(Date date, int day) {
        return add(date, Calendar.DAY_OF_YEAR, day);
    }
 
    /**
     * 指定日期加减星期
     * 
     * @param date 指定日期
     * @param week 变更星期数（大于 0 时增加，小于 0 时减少）
     * @return 变更星期数后的日期
     */
    public static Date addWeek(Date date, int week) {
        return add(date, Calendar.WEEK_OF_YEAR, week);
    }
 
    /**
     * 指定日期加减小时
     * 
     * @param date 指定日期时间
     * @param hour 变更小时数（大于 0 时增加，小于 0 时减少）
     * @return 变更小时数后的日期时间
     */
    public static Date addHour(Date date, int hour) {
        return add(date, Calendar.HOUR_OF_DAY, hour);
    }
 
    /**
     * 指定日期加减分钟
     * 
     * @param date   指定日期时间
     * @param minute 变更分钟数（大于 0 时增加，小于 0 时减少）
     * @return 变更分钟数后的日期时间
     */
    public static Date addMinute(Date date, int minute) {
        return add(date, Calendar.MINUTE, minute);
    }
 
    /**
     * 指定日期加减秒
     * 
     * @param date   指定日期时间
     * @param second 变更秒数（大于 0 时增加，小于 0 时减少）
     * @return 变更秒数后的日期时间
     */
    public static Date addSecond(Date date, int second) {
        return add(date, Calendar.SECOND, second);
    }
 
    /**
     * 指定日期加减秒
     * 
     * @param date   指定日期时间
     * @param millisecond 变更毫秒数（大于 0 时增加，小于 0 时减少）
     * @return 变更毫秒数后的日期时间
     */
    public static Date addMillisecond(Date date, int millisecond) {
        return add(date, Calendar.MILLISECOND, millisecond);
    }
 
    /**
     * 获取该日期所在周指定星期的日期
     * 
     * @param date 日期所在时间
     * @return index 指定星期（1 - 7 分别对应星期一到星期天）
     */
    public static Date getWeekDate(Date date, int index) {
        if (index < WEEK_1_MONDAY || index > WEEK_7_SUNDAY) {
            return null;
        }
        int week = getWeek(date);
        return addDay(date, index - week);
    }
 
    /**
     * 获取该日期所在周开始日期
     * 
     * @param date 日期所在时间
     * @return 所在周开始日期
     */
    public static Date getWeekDateStart(Date date) {
        return getDateStart(getWeekDate(date, WEEK_1_MONDAY));
    }
 
    /**
     * 获取该日期所在周开始日期
     * 
     * @param date 日期所在时间
     * @return 所在周开始日期
     */
    public static Date getWeekDateEnd(Date date) {
        return getWeekDateEnd(getWeekDate(date, WEEK_7_SUNDAY));
    }
 
    /**
     * 获取该日期所在周的所有日期（周一到周日）
     * 
     * @param date 日期
     * @return 该日照所在周的所有日期
     */
    public static List<Date> getWeekDateList(Date date) {
        if (Objects.isNull(date)) {
            return Collections.emptyList();
        }
        // 获取本周开始时间
        Date weekFromDate = getWeekDateStart(date);
        // 获取本周截止时间
        Date weekeEndDate = getWeekDateEnd(date);
        return getBetweenDateList(weekFromDate, weekeEndDate, true);
    }
 
    /**
     * 获取该日期所在周的所有日期（周一到周日）
     * 
     * @param dateString
     * @return 该日照所在周的所有日期
     */
    public static List<String> getWeekDateList(String dateString) {
        Date date = parseDate(dateString);
        if (Objects.isNull(date)) {
            return Collections.emptyList();
        }
        return getDateStrList(getWeekDateList(date));
    }
 
    /**
     * 获取该日期所在月的所有日期
     * 
     * @param date
     * @return 该日照所月的所有日期
     */
    public static List<Date> getMonthDateList(Date date) {
        if (Objects.isNull(date)) {
            return Collections.emptyList();
        }
        Date monthDateStart = getMonthDateStart(date);
        Date monthDateEnd = getMonthDateEnd(date);
        return getBetweenDateList(monthDateStart, monthDateEnd, true);
    }
 
    /**
     * 获取该日期所在月的所有日期
     * 
     * @param dateString
     * @return 该日照所月的所有日期
     */
    public static List<String> getMonthDateList(String dateString) {
        Date date = parseDate(dateString);
        if (Objects.isNull(date)) {
            return Collections.emptyList();
        }
        return getDateStrList(getMonthDateList(date));
    }
    
    /**
     * 获取本日期所在月第一天
     * 
     * @param date 日期
     * @return 本日期所在月第一天
     */
    public static Date getMonthDateStart(Date date) {
        if (Objects.isNull(date)) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return getDateStart(calendar.getTime());
    }
 
    /**
     * 获取本日期所在月最后一天
     * 
     * @param date 日期
     * @return 本日期所在月最后一天
     */
    public static Date getMonthDateEnd(Date date) {
        if (Objects.isNull(date)) {
            return null;
        }
        Date monthDateStart = getMonthDateStart(date);
        Date nextMonthDateStart = getMonthDateStart(addMonth(monthDateStart, 1));
        return getDateEnd(addDay(nextMonthDateStart, -1));
    }
    
    /**
     * 获取两个日期相差的秒数
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 相差秒数（若返回 -1，则至少有一个日期存在为空，此时不能进行比较）
     */
    public static long countBetweenSecond(Date date1, Date date2) {
        if (Objects.isNull(date1) || Objects.isNull(date2)) {
            return -1;
        }
        // 直接计算毫秒差并转换为秒  
        long diffInMilliseconds = Math.abs(date2.getTime() - date1.getTime());
        return diffInMilliseconds / 1000;
    }
    
 
    /**
     * 获取两个日期之间的所有日期
     * 
     * @param date1 日期1
     * @param date2 日期2
     * @return 两个日期之间的所有日期的开始时间
     */
    public static List<Date> getBetweenDateList(Date date1, Date date2, boolean isContainParams) {
        if (Objects.isNull(date1) || Objects.isNull(date2)) {
            return Collections.emptyList();
        }
        // 确定前后日期
        Date fromDate = date1;
        Date toDate = date2;
        if (date2.before(date1)) {
            fromDate = date2;
            toDate = date1;
        }
        // 获取两个日期每天的开始时间
        Date from = getDateStart(fromDate);
        Date to = getDateStart(toDate);
        // 获取日期，开始循环
        List<Date> dates = new ArrayList<Date>();
        if (isContainParams) {
            dates.add(from);
        }
        Date date = from;
        boolean isBefore = true;
        while (isBefore) {
            date = addDay(date, 1);
            isBefore = date.before(to);
            if (isBefore) {
                dates.add(getDateStart(date));
            }
        }
        if (isContainParams) {
            dates.add(to);
        }
        return dates;
    }
 
    /**
     * 获取两个日期之间的所有日期
     * 
     * @param dateString1 日期1（如：2022-06-20）
     * @param dateString2 日期2（如：2022-07-15）
     * @return 两个日期之间的所有日期（不包含参数日期）
     */
    public static List<String> getBetweenDateList(String dateString1, String dateString2) {
        return getBetweenDateList(dateString1, dateString2, false);
    }
 
    /**
     * 获取两个日期之间的所有日期
     * 
     * @param dateString1     日期1（如：2022-06-20）
     * @param dateString2     日期2（如：2022-07-15）
     * @param isContainParams 是否包含参数的两个日期
     * @return 两个日期之间的所有日期的开始时间
     */
    public static List<String> getBetweenDateList(String dateString1, String dateString2, boolean isContainParams) {
        Date date1 = parseDate(dateString1);
        Date date2 = parseDate(dateString2);
        List<Date> dates = getBetweenDateList(date1, date2, isContainParams);
        return getDateStrList(dates);
    }
 
    /**
     * List<Date> 转 List<String>
     * 
     * @param dates 日期集合
     * @return 日期字符串集合
     */
    public static List<String> getDateStrList(List<Date> dates) {
        if (dates.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> dateList = new ArrayList<String>();
        for (Date date : dates) {
            dateList.add(formatDate(date));
        }
        return dateList;
    }
    
    /**
     * 
     * timeZoneOffset表示时区，如中国一般使用东八区，因此timeZoneOffset就是8
     *
     * @param timeZoneOffset
     * @return
     */
    public static String getFormatedDateString(float timeZoneOffset, String pattern) {
        int thirteen = 13;
        int minusTwelve = -12;
        if (timeZoneOffset > thirteen || timeZoneOffset < minusTwelve) {
            timeZoneOffset = 0;
        }
        
        int newTime = (int) (timeZoneOffset * 60 * 60 * 1000);
        TimeZone timeZone;
        String[] ids = TimeZone.getAvailableIDs(newTime);
        if (ids.length == 0) {
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = new SimpleTimeZone(newTime, ids[0]);
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(timeZone);
        return sdf.format(new Date());
    }
    @Data
    static class DateNode {
        /** 年 */
        private int year;
        /** 月 */
        private int month;
        /** 日 */
        private int day;
        /** 时 */
        private int hour;
        /** 分 */
        private int minute;
        /** 秒 */
        private int second;
        /** 毫秒 */
        private int millisecond;
        /** 星期几（ 1 - 7 对应周一到周日） */
        private int week;
        /** 当年第几天 */
        private int dayOfYear;
        /** 当年第几周（本年周 1 为第 1 周，0 则表示属于去年最后一周） */
        private int weekOfYear;
        /** 当年第几周（本年周 1 为第 1 周，0 则表示属于去年最后一周） */
        private int weekOfYearIgnoreLastYear;
        /** 时间戳（秒级） */
        private long secondStamp;
        /** 时间戳（毫秒级） */
        private long millisecondStamp;
        /** 显示时间 */
        private String time;
 
    }
    
    /**
     * 将符合相应格式的字符串转化为日期 <格式自定义>
     *
     * @param dateStr 日期字符串
     * @param pattern 日期格式
     * @return Date 返回类型 日期字串为空或者不符合日期格式时返回null
     */
    public static Date getDate(String dateStr, String pattern) {
        return getDate(dateStr, pattern, null);
    }
    
    /**
     * 将符合相应格式的字符串转化为日期 <格式自定义>
     *
     * @param dateStr     日期字符串
     * @param pattern     日期格式
     * @param defaultDate 默认日期
     * @return Date 返回类型 日期字串为空或者不符合日期格式时返回null
     */
    public static Date getDate(String dateStr, String pattern, Date defaultDate) {
        if (dateStr != null && pattern != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                return sdf.parse(dateStr);
            } catch (ParseException e) {
                throw new IllegalArgumentException("字符串转化为日期失败！", e);
            }
        }
        return defaultDate;
    }
}