package com.xiaobiao.utils.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;

import java.text.ParsePosition;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DateUtils {

    /** 默认日期格式 */
    public static final String DATE_Y_M_D_8 = "yyyy-MM-dd";
    public static final String DATE_YMDHMS_SPLIT_14 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_YMD_8 = "yyyyMMdd";
    public static final String DATE_YM_6 = "yyyyMM";
    public static final String DATE_YMDHSM_14 = "yyyyMMddHHssmm";
    public static final String DATE_YMDHMS_14 = "yyyyMMddHHmmss";
    public static final String DATETO6BITYMD = "yyMMdd";
    /**
     * 得到指定月的最后一天的日期
     * @param month yyyyMM   如：201309  返回  20130930
     */
    public static String getLastDayFromMonth(String month){
        String result = "";
        if(month.length() != 6){
            return result;
        }
        DateTime dt = DateTime.parse(month, DateTimeFormat.forPattern(DATE_YM_6));
        result = month + "" + dt.dayOfMonth().getMaximumValue();
        return result;
    }


    /**
     * 得到指定月的第一天的日期
     * @param month yyyyMM   如：201309  返回  20130901
     */
    public static String getFirstDayFromMonth(String month){
        String result = "";
        if(month.length() != 6){
            return result;
        }
        DateTime dt = DateTime.parse(month,DateTimeFormat.forPattern(DATE_YM_6));
        result = month + "0" + dt.dayOfMonth().getMinimumValue();
        return result;
    }

    /**
     * 得到今天指定格式的日期
     * @param format 指定格式
     * @return 当天日期
     */
    public static String getToDay(String format){
        if(StringUtils.isEmpty(format)){
            format = DATE_YMD_8;
        }
        DateTime dt = new DateTime();
        return dt.toString(format);
    }

    /**
     * 获取当日0时0分0秒日期(页面日期控件)
     * @return 当日0时0分0秒日期
     */
    public static String getTodayBegin_14() {
        String date_8 = getToDay(DATE_Y_M_D_8);
        return date_8.concat(" 00:00:00");
    }

    /**
     * 获取当日23时59分59秒日期(页面日期控件)
     * @return 当日23时59分59秒日期
     */
    public static String getTodayEnd_14() {
        String date_8 = getToDay(DATE_Y_M_D_8);
        return date_8.concat(" 23:59:59");
    }

    /**
     * 得到昨天的日期
     * @return YYYYMMDD
     */
    public static String getYesterdayDate(String format){
        if(StringUtils.isEmpty(format)){
            format= DATE_YMD_8;
        }
        return DateUtils.getAroundDateByDay(-1, DateUtils.getToDay(format), format);
    }

    /**
     * 得到指定日期的相隔日期
     * @param cycleNum   相隔多少天
     * @param inDate	 传入的时间，格式：yyyyMMdd
     * @param format     传出的时间格式
     */
    public static String getAroundDateByDay(int cycleNum, String inDate, String format) {
        if(StringUtils.isEmpty(format)){
            format= DATE_YMD_8;
        }
        inDate = inDate.substring(0, 4) + "-" + inDate.substring(4, 6) + "-" + inDate.substring(6, 8);
        DateTime dt = new DateTime(inDate);
        DateTime dtime = dt.plusDays(cycleNum);
        return dtime.toString(format);
    }

    public static DateTime getDateTime(String inDate,String format){
        if(StringUtils.isEmpty(format)){
            format= DATE_YMD_8;
        }
        return DateTime.parse(inDate, DateTimeFormat.forPattern(format));
    }

    /**
     * 得到指定日期的相隔日期
     * @param cycleNum   相隔多少小时
     * @param inDate	 传入的时间，格式：yyyyMMdd
     * @param format     传出的时间格式
     */
    public static String getAroundDateByHour(int cycleNum,String inDate,String format){
        if(StringUtils.isEmpty(format)){
            format= DATE_YMD_8;
        }
        DateTime dt = new DateTime(Integer.valueOf(inDate.substring(0, 4)),Integer.valueOf(inDate.substring(4, 6)),Integer.valueOf(inDate.substring(6, 8)),Integer.valueOf(inDate.substring(8, 10)),Integer.valueOf(inDate.substring(10, 12)),Integer.valueOf(inDate.substring(12, 14)),0);
        DateTime dtime = dt.minusHours(cycleNum);
        return dtime.toString(format);
    }

    /**
     * 得到指定日期的相隔年数的日期
     * @param cycleNum   相隔多少年
     * @param inDate	传入的时间，格式：yyyyMMdd
     * @param format	传出的时间格式
     */
    public static String getAroundDateByYear(int cycleNum, String inDate, String format) {
        if(StringUtils.isEmpty(format)){
            format= DATE_YMD_8;
        }
        DateTime dt = new DateTime(Integer.valueOf(inDate.substring(0, 4)),Integer.valueOf(inDate.substring(4, 6)),Integer.valueOf(inDate.substring(6, 8)),0,0,0,0);
        DateTime dtime = dt.plusYears(cycleNum);
        return dtime.toString(format);
    }

    /**
     * 得到两个时间相隔的天数
     */
    public static int getAroundDate(String beginDate, String endDate){
        return Days.daysBetween(getDateTime(beginDate, DATE_YMD_8), getDateTime(endDate, DATE_YMD_8)).getDays();
    }

    /**
     * 得到指定日期的相隔月数的日期
     * @param cycleNum   相隔多少年
     * @param inDate	传入的时间，格式：yyyyMMdd
     * @param format	传出的时间格式
     */
    public static String getAroundDateByMonth(int cycleNum, String inDate, String format){
        if(StringUtils.isEmpty(format)){
            format= DATE_YMD_8;
        }
        DateTime dt = new DateTime(Integer.valueOf(inDate.substring(0, 4)),Integer.valueOf(inDate.substring(4, 6)),Integer.valueOf(inDate.substring(6, 8)),
                Integer.valueOf(inDate.substring(8, 10)),Integer.valueOf(inDate.substring(10, 12)),Integer.valueOf(inDate.substring(12, 14)));
        DateTime dtime = dt.plusMonths(cycleNum);
        return dtime.toString(format);
    }

    /**
     * 得到两个时间相隔的秒
     */
    public static Long getAroundMinute(String beginDate,String endDate){
        return (long) Seconds.secondsBetween(getDateTime(beginDate, DateUtils.DATE_YMDHMS_14), getDateTime(endDate, DateUtils.DATE_YMDHMS_14)).getSeconds();
    }

    /**
     * 日期转换
     */
    public static java.sql.Date convertSQLDate(String date) {
        try {
            if(date == null){
                return null;
            }
            FastDateFormat formatDate = FastDateFormat.getInstance(DATE_YMD_8);
            return new java.sql.Date(((java.sql.Date)formatDate.parseObject(date)).getTime());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 计算两个日期的间隔天数
     * @param startDate
     *            开始时间，如：2008-12-03 11:00:00
     * @param endDate
     *            结束时间，如：2009-12-31 11:00:00
     * @return long 间隔天数(long)
     */
    public static long getBetweenDays(Date startDate, Date endDate) {
        if (endDate == null || startDate == null){
            return -1;
        }
        Long days = endDate.getTime() - startDate.getTime();
        days = days/(1000*60*60*24);
        return days;
    }

    /**
     * 验证输入的文本信息日期是否合
     */
    public static Date isDate(String dateStr) {
        String date_format_1 = "yyyy/MM/dd";
        String date_format_2 = "yyyy-MM-dd";
        String date_format_3 = "yyyyMMdd";
        String date_format_4 = "yyyy.MM.dd";
        String[] date_format = { date_format_1, date_format_2, date_format_3, date_format_4 };
        for (String sDate : date_format) {
            Date tempDate = DateUtils.isDate(dateStr, sDate);
            if (null != tempDate) {
                return tempDate;
            }
        }
        return null;
    }

    /**
     * 验证输入的文本信息日期是否合
     */
    public static boolean isDateByEl(String dateStr,String eL){
        Pattern p = Pattern.compile(eL);
        Matcher m = p.matcher(dateStr);
        return m.matches();
    }

    /**
     * 验证输入的文本信息日期是否合
     */
    public static Date isDate(String dateStr, String patternString) {
        if(StringUtils.isEmpty(patternString)){
            patternString= DateUtils.DATE_YMD_8;
        }
        try {
            FastDateFormat formatDate = FastDateFormat.getInstance(patternString);
            ParsePosition pos = new ParsePosition(0);
            Date tempDate = (Date) formatDate.parseObject(dateStr, pos);
            tempDate.getTime();
            return tempDate;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * date 转String
     */
    public static String pareDate(Date date,String pattern){
        if(null == date){
            return null;
        }
        if(StringUtils.isEmpty(pattern)){
            pattern= DATE_YMD_8;
        }
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * String 转date
     */
    public static Date pareDate(String dateStr,String pattern){
        if(null == dateStr){
            return null;
        }
        if(StringUtils.isEmpty(pattern)){
            pattern= DATE_YMD_8;
        }
        DateTime dt = DateTime.parse(dateStr, DateTimeFormat.forPattern(pattern));
        return dt.toDate();
    }

    /**
     * 把字符串转换成对应格式的DateTime
     * 如果默认时间填写，则转换失败时返回默认时间
     * @param dateStr
     * @param pattern
     * @param defaultDateTime
     * @return
     */
    public static DateTime parseDateTime(String dateStr, String pattern, DateTime... defaultDateTime) {
        if(defaultDateTime==null || defaultDateTime.length == 0){
            return DateTimeFormat.forPattern(pattern).parseDateTime(dateStr);
        }
        try {
            DateTime dateTime = DateTimeFormat.forPattern(pattern).parseDateTime(dateStr);
            return dateTime;
        } catch (Exception ex) {
            return defaultDateTime[0];
        }
    }

    /**
     * 格式化Date
     */
    public static Date formatDate(Date date,String pattern){
        if(null == date){
            return null;
        }
        if(StringUtils.isEmpty(pattern)){
            pattern= DATE_YMD_8;
        }
        String dateStr = DateFormatUtils.format(date, pattern);
        return pareDate(dateStr, pattern);
    }


    /**
     * 当前月往前推
     * @param date 日期
     * @return Date 日期
     */
    public static Date getLastMonth(Date date,int cycle) {
        DateTime datetime = new DateTime(date);
        return datetime.minusMonths(cycle).toDate();
    }

    /**
     * 工具方法
     * 计算获得往后推进完成后的日期 若计算后日期大于今天则返回今天
     * @param incrementDay 增加天数
     * @param referentialEndDate 作为参考的结束日期
     * @return 计算过后的日期 = ((referentialEndDate + incrementDay) > 今天 ? 今天 : referentialEndDate + incrementDay)
     * @throws Exception
     * 2014-09-04 蒯越
     */
    public static String getCalculatedEndTime(int incrementDay, String referentialEndDate) throws Exception {
        String endTime = DateUtils.getAroundDateByDay(incrementDay, referentialEndDate, "yyyyMMdd");
        long nowTimeL = new Date().getTime();
        long endTimeL = DateUtils.pareDate(endTime, "yyyyMMdd").getTime();
        if(nowTimeL > endTimeL){
            // 今天大于清算结束日期, 使用清算结束日期
            return endTime;
        }else{
            // 今天小于清算结束日期, 使用今天
            return DateUtils.getToDay("yyyyMMdd");
        }
    }

    /**
     * 根据传入参数判断是否是当天
     * @param date 传入日期
     * @param patten 传入日期格式
     * @return true 是当天 false 不是当天
     * @autor 陶伟超
     */
    public static boolean isCurrentDate(Date date,String patten){
        return DateUtils.formatDate(new Date(), patten).compareTo(DateUtils.formatDate(date,patten)) == 0;
    }

    /**
     * 传入两个时间，得到两者时间的相差几时几秒
     */
    public static String getBetweenTime(Date beforeTime,Date afterTime){
        long ms = afterTime.getTime() - beforeTime.getTime();

        long ss = 1000;//1秒 = 1000毫秒
        long mi = ss * 60;
        long hh = mi * 60;
        long dd = hh * 24;

        String result = "";
        if(ms % dd > 0 && ms > 0){
            result += (ms - ms % dd) / dd+"天";
            ms = ms % dd;
        }
        if(ms % hh > 0 && ms > 0){
            result += (ms - ms % hh) / hh+"时";
            ms = ms % hh;
        }
        if(ms % mi > 0 && ms > 0){
            result += (ms - ms % mi) / mi+"分";
            ms = ms % mi;
        }
        if(ms % ss > 0 && ms > 0){
            result += (ms - ms % ss) / ss+"秒";
            ms = ms % ss;
        }
        return result;
    }
}
