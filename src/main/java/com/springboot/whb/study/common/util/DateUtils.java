package com.springboot.whb.study.common.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: whb
 * @date: 2019/7/12 11:06
 * @description: 时间工具类
 */
public class DateUtils {
    private static Logger logger = LoggerFactory.getLogger(DateUtils.class);
    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String timePattern = "HH:mm";

    public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }


    /**
     * Return default datePattern (MM/dd/yyyy)
     *
     * @return a string representing the date pattern on the UI
     */
    public static String getDatePattern() {
        Locale locale = LocaleContextHolder.getLocale();
        String defaultDatePattern;
        try {
            defaultDatePattern = ResourceBundle.getBundle("ApplicationResources", locale).getString("date.format");
        } catch (MissingResourceException mse) {
            defaultDatePattern = "yyyy-MM-dd";
        }

        return defaultDatePattern;
    }

    public static String getNow() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String nowstr = df.format(new Date());
        return nowstr;
    }

    public static String getNow(String formatType) {
        SimpleDateFormat df = new SimpleDateFormat(formatType);
        String nowstr = df.format(new Date());
        return nowstr;
    }

    public static String getDateTimePattern() {
        return getDatePattern() + " HH:mm:ss";
    }

    /**
     * This method attempts to convert an Oracle-formatted date in the form
     * dd-MMM-yyyy to mm/dd/yyyy.
     *
     * @param aDate date from database as a string
     * @return formatted string for the ui
     */
    public static String getDate(Date aDate) {
        SimpleDateFormat df;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(getDatePattern());
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getDateTimeNow() {
        SimpleDateFormat df;
        String returnValue = "";

        df = new SimpleDateFormat(getDateTimePattern());
        returnValue = df.format(new Date());

        return (returnValue);
    }

    /**
     * This method generates a string representation of a date/time in the
     * format you specify on input
     *
     * @param aMask   the date pattern the string is in
     * @param strDate a string representation of a date
     * @return a converted Date object
     * @throws ParseException
     * @see SimpleDateFormat
     */
    public static Date convertStringToDate(String aMask, String strDate) throws ParseException {
        SimpleDateFormat df;
        Date date;
        df = new SimpleDateFormat(aMask);

        if (logger.isDebugEnabled()) {
            logger.debug("converting '" + strDate + "' to date with mask '" + aMask + "'");
        }

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            // log.error("ParseException: " + pe);
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }

        return (date);
    }

    /**
     * This method returns the current date time in the format: MM/dd/yyyy HH:MM
     * a
     *
     * @param theTime the current time
     * @return the current date/time
     */
    public static String getTimeNow(Date theTime) {
        return getDateTime(timePattern, theTime);
    }

    public static String getThisMonthStr() {
        return getDateTime("yyyyMM", new Date());
    }

    /**
     * This method returns the current date in the format: MM/dd/yyyy
     *
     * @return the current date
     * @throws ParseException
     */
    public static Calendar getToday() throws ParseException {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat(getDatePattern());

        // This seems like quite a hack (date -> string -> date),
        // but it works ;-)
        String todayAsString = df.format(today);
        Calendar cal = new GregorianCalendar();
        cal.setTime(convertStringToDate(todayAsString));

        return cal;
    }

    /**
     * This method generates a string representation of a date's date/time in
     * the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param aDate a date object
     * @return a formatted string representation of the date
     * @see SimpleDateFormat
     */
    public static String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate == null) {
            logger.error("aDate is null!");
        } else {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * This method generates a string representation of a date based on the
     * System Property 'dateFormat' in the format you specify on input
     *
     * @param aDate A date to convert
     * @return a string representation of the date
     */
    public static String convertDateToString(Date aDate) {
        return getDateTime(getDatePattern(), aDate);
    }

    /**
     * This method converts a String to a date using the datePattern
     *
     * @param strDate the date to convert (in format MM/dd/yyyy)
     * @return a date object
     * @throws ParseException
     */
    public static Date convertStringToDate(String strDate) throws ParseException {
        Date aDate = null;
        if (strDate == null || "".equals(strDate.trim())) {
            return null;
        }
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("converting date with pattern: " + getDatePattern());
            }
            aDate = convertStringToDate(getDatePattern(), strDate);
        } catch (ParseException pe) {
            logger.error("Could not convert '" + strDate + "' to a date, throwing exception");
            pe.printStackTrace();
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }
        return aDate;
    }

    /**
     * 获取昨天的时间
     *
     * @return
     * @author hb.wang 2016-3-28
     */
    public static String getYesterdayDateStr() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date d1 = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat(getDatePattern());
        String yesterday = df.format(d1);
        return yesterday;
    }

    /**
     * 获取昨天的开始时间
     *
     * @return
     * @author hb.wang 2016-3-28
     */
    public static Date getYesterdayStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date today = cal.getTime();
        return today;
    }

    /**
     * 获取当天的时间
     *
     * @return
     * @author hb.wang 2016-3-28
     */
    public static String getTodayDateStr() {
        Calendar cal = Calendar.getInstance();
        Date d1 = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat(getDatePattern());
        String today = df.format(d1);
        return today;
    }

    /**
     * 获取当天的开始时间
     *
     * @return
     * @author hb.wang 2016-3-28
     */
    public static Date getTodayStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date today = cal.getTime();
        return today;
    }

    /**
     * 获取当月第一天
     *
     * @return
     */
    public static String getFirstOfMonthDateStr() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        Date d1 = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat(getDatePattern());
        String today = df.format(d1);
        return today;
    }

    /**
     * 求某个月的最后一天
     */
    public static int getMonthEndDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int maxDate = cal.getLeastMaximum(Calendar.DATE);
        return maxDate;
    }

    /**
     * getMonthEnd
     * <p>
     * 取给定日期所在月的最后一天
     * </p>
     *
     * @param strdate 2009-10-01
     * @return String 31
     * @author whb
     */
    public static String getMonthEnd(String strdate, String patten) {
        String result = "";
        try {
            Date date = getFormatDate((getMonthBegin(strdate, patten)), patten);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            result = formatDateByFormat(calendar.getTime(), "yyyy-MM-dd");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * getMonthBegin
     * <p>
     * 取给定日期所在月的第一天
     * </p>
     *
     * @param strdate
     * @return String
     * @throws ParseException
     * @author whb
     */
    public static String getMonthBegin(String strdate, String patten) {
        Date date = getFormatDate(strdate, patten);
        return formatDateByFormat(date, "yyyy-MM") + "-01";
    }

    public static String formatDateByFormat(Date date, String patten) {
        SimpleDateFormat t = new SimpleDateFormat(patten);
        return t.format(date);
    }

    public static Date parseFormatDate(String date) throws ParseException {
        SimpleDateFormat t = new SimpleDateFormat("yyyy-MM-dd");
        return t.parse(date);
    }

    public static String formatDateFormat(Date date, String patten) {
        SimpleDateFormat t = new SimpleDateFormat(patten);
        return t.format(date);
    }

    /**
     * 获取哪种格式的日期
     *
     * @param date
     * @param format
     * @return
     */
    public static String getDateFormat(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        String dates = df.format(date);
        return dates;
    }

    /**
     * 求下月
     *
     * @param date
     * @return
     */
    public static String getNextMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        return formatDateByFormat(cal.getTime(), "yyyy-MM");
    }

    /**
     * 求下月
     *
     * @return
     */
    public static String getNextMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        return formatDateByFormat(cal.getTime(), "yyyy-MM");
    }

    /**
     * @param date
     * @param num
     * @return
     * @throws
     * @Description: 获取增加num之后的月份
     * @author whb
     * @date 2017年7月19日
     */
    public static String getMonth(Date date, int num) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, num);
        return formatDateByFormat(cal.getTime(), "MM");
    }

    /**
     * @param date
     * @param num
     * @return
     * @throws
     * @Description: 获取增加num之后的日期
     * @author whb
     * @date 2017年8月11日
     */
    public static Date getDay(Date date, int num) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, num);
        return cal.getTime();
    }

    /**
     * 取指定时间的下一天
     *
     * @param date
     * @return
     */
    public static Date getNextDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }

    /**
     * 将字符串转成日期类型，格式需传进来
     *
     * @param dates  字符型日期
     * @param format 日期格式
     * @return Date
     */
    public static Date getFormatDate(String dates, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = df.parse(dates);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取某个的四个季度
     *
     * @return
     */
    public static String[] getYearFourQuarter(String year) {
        String oneBeginQuarter = year + "-01-01";
        String oneEndQuarter = year + "-03-31";
        String twoBeginQuarter = year + "-04-01";
        String twoEndQuarter = year + "-06-30";
        String threeBeginQuarter = year + "-07-01";
        String threeEndQuarter = year + "-09-30";
        String fourBeginQuarter = year + "-10-01";
        String fourEndQuarter = year + "-12-31";
        String[] quarter = new String[4];
        quarter[0] = oneBeginQuarter + "," + oneEndQuarter;
        quarter[1] = twoBeginQuarter + "," + twoEndQuarter;
        quarter[2] = threeBeginQuarter + "," + threeEndQuarter;
        quarter[3] = fourBeginQuarter + "," + fourEndQuarter;
        return quarter;
    }

    /**
     * @param year
     * @return
     * @throws
     * @Description: 获取指定年份一四个季度时间段（公安）
     * @author whb
     * @date 2017年4月13日
     */
    public static String[] getYearFourQuarterForPolice(String year) {
        String oneBeginQuarter = Integer.parseInt(year) - 1 + "-12-26";
        String oneEndQuarter = year + "-03-25";
        String twoBeginQuarter = year + "-03-26";
        String twoEndQuarter = year + "-06-25";
        String threeBeginQuarter = year + "-06-26";
        String threeEndQuarter = year + "-09-25";
        String fourBeginQuarter = year + "-09-26";
        String fourEndQuarter = year + "-12-25";
        String[] quarter = new String[4];
        quarter[0] = oneBeginQuarter + "," + oneEndQuarter;
        quarter[1] = twoBeginQuarter + "," + twoEndQuarter;
        quarter[2] = threeBeginQuarter + "," + threeEndQuarter;
        quarter[3] = fourBeginQuarter + "," + fourEndQuarter;
        return quarter;
    }

    @SuppressWarnings("rawtypes")
    private static HashMap hashMap = new HashMap();

    /**
     * 获取当前星期
     *
     * @return
     */
    public static int getDayOfWeek(Date date) {
        Calendar aCalendar = Calendar.getInstance();
        // 里面也可以直接插入date类型
        aCalendar.setTime(date);
        // 计算此日期是一周中的哪一天
        int x = aCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        return x;
    }

    /**
     * 通过给定string型的日期，获取当前星期几
     *
     * @param date
     * @return 当前星期几
     */
    public static String getWeek(Date date) {
        int week = getDayOfWeek(date);
        setWeek();
        return hashMap.get(week).toString();
    }

    /**
     * 数字对应的星期几
     */
    @SuppressWarnings("unchecked")
    private static void setWeek() {
        hashMap.put(0, "星期天");
        hashMap.put(1, "星期一");
        hashMap.put(2, "星期二");
        hashMap.put(3, "星期三");
        hashMap.put(4, "星期四");
        hashMap.put(5, "星期五");
        hashMap.put(6, "星期六");
    }

    /**
     * 将字符串转成日期类型，格式需传进来
     *
     * @param dates  字符型日期
     * @param format 日期格式
     * @return Date
     * @author whb
     */
    public static String getFormatDate(Date dates, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        String date = null;
        date = df.format(dates);
        return date;
    }

    /**
     * 日期中文大写格式
     *
     * @author whb
     */
    private static final String[] NUMBERS =
            {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

    /**
     * 通过 yyyy-MM-dd 得到中文大写格式 yyyy MM dd 日期
     */
    public static synchronized String toChinese(String str) {
        StringBuffer sb = new StringBuffer();
        sb.append(getSplitDateStr(str, 0)).append("年").append(getSplitDateStr(str, 1)).append("月").append(getSplitDateStr(str, 2))
                .append("日");
        return sb.toString();
    }

    /**
     * 分别得到年月日的大写 默认分割符 "-"
     */
    public static String getSplitDateStr(String str, int unit) {
        // unit是单位 0=年 1=月 2日
        String[] DateStr = str.split("-");
        if (unit > DateStr.length) {
            unit = 0;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < DateStr[unit].length(); i++) {

            if ((unit == 1 || unit == 2) && Integer.valueOf(DateStr[unit]) > 9) {
                sb.append(convertNum(DateStr[unit].substring(0, 1))).append("十").append(convertNum(DateStr[unit].substring(1, 2)));
                break;
            } else {
                sb.append(convertNum(DateStr[unit].substring(i, i + 1)));
            }
        }
        if (unit == 1 || unit == 2) {
            return sb.toString().replaceAll("^一", "").replace("O", "");
        }
        return sb.toString();

    }

    /**
     * 转换数字为大写
     */
    private static String convertNum(String str) {
        return NUMBERS[Integer.valueOf(str)];
    }

    private static final Pattern numericPattern = Pattern.compile("[0-9]*");

    /**
     * 判断是否是零或正整数
     */
    public static boolean isNumeric(String str) {
        Matcher isNum = numericPattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 求两个指定字符串日期的相差周数
     *
     * @param startDate
     * @return
     */
    public static long getTodayIntevalDays(String startDate, String endDate) {
        try {
            // 指定日期
            SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Date theDate = myFormatter.parse(startDate);
            Date endDates = myFormatter.parse(endDate);
            // 两个时间之间的天数
            long days = (endDates.getTime() - theDate.getTime()) / (24 * 60 * 60 * 1000);
            return days / 7;
        } catch (Exception ee) {
            return 0L;
        }
    }

    /**
     * 获取当前时间（到毫秒）
     *
     * @return
     */
    public static String getNowMillon() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String nowstr = df.format(new Date());
        return nowstr;
    }

    /**
     * 通过日期获取当年的时间
     *
     * @param date
     * @return
     * @author whb
     * @create Jun 9, 2013 10:06:22 AM
     */
    @SuppressWarnings("deprecation")
    public static String[] getYearByDays(Date date) {
        String[] days = new String[2];
        int startY = date.getYear() + 1900;
        // 如果开始日期为20120112则开始日期为20111226
        days[0] = (startY - 1) + "-" + 12 + "-" + 26;
        // 计算周期的结束日期
        days[1] = startY + "-" + 12 + "-" + 25;
        return days;
    }

    /**
     * 两个日期相差多少天
     *
     * @param beginDate--开始时间
     * @param endDate--结束时间
     * @return
     */
    public static long betweenDays(Date beginDate, Date endDate) {
        long l1 = beginDate.getTime();
        long l2 = endDate.getTime();
        // long days = Math.abs((l2 - l1) / (1000 * 60 * 60 * 24));
        long days = (l2 - l1) / (1000 * 60 * 60 * 24);
        return days;
    }

    /**
     * 两个日期相差多少小时
     *
     * @param beginDate
     * @param endDate
     * @return
     * @author hb.wang 2016-7-1
     */
    public static long beweenHours(Date beginDate, Date endDate) {
        long l1 = beginDate.getTime();
        long l2 = endDate.getTime();
        long hours = (l2 - l1) / (1000 * 60 * 60);
        return hours;
    }

    /**
     * 两个日期相差多少分钟
     *
     * @param beginDate--开始时间
     * @param endDate--结束时间
     * @return
     * @author hb.wang 2016-3-8
     */
    public static long betweenMinutes(Date beginDate, Date endDate) {
        long l1 = beginDate.getTime();
        long l2 = endDate.getTime();
        long minutes = (l2 - l1) / (1000 * 60);
        return minutes;
    }

    /**
     * 获取当年的第一天
     *
     * @return
     * @author hb.wang 2016-3-3
     */
    public static Date getCurrYearFirst() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取当年的最后一天
     *
     * @return
     * @author hb.wang 2016-3-3
     */
    public static Date getCurrYearLast() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }

    /**
     * 获取某年第一天的日期
     *
     * @param year--年份
     * @return
     * @author hb.wang 2016-3-3
     */
    public static Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取某年的最后一天日期
     *
     * @param year--年份
     * @return
     * @author hb.wang 2016-3-3
     */
    public static Date getYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();
        return currYearLast;
    }

    /**
     * 返回指定日期的月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 返回指定日期的月的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        return calendar.getTime();
    }

    /**
     * 取得当前日期所在周的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        // Sunday
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6);
        return calendar.getTime();
    }

    /**
     * 取得当前日期所在周的星期五
     *
     * @param date
     * @return
     */
    public static Date getFridayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        //Friday
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 4);
        return calendar.getTime();
    }

    /**
     * 取得当前日期所在周的星期六
     *
     * @param date
     * @return
     */
    public static Date getSaturdayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        // Saturday
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 5);
        return calendar.getTime();
    }

    /**
     * 取得当前日期所在周的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        // Monday
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return calendar.getTime();
    }

    /**
     * @param startTime
     * @param endTime
     * @return
     * @throws
     * @Description: 获取指定日期范围内的所有日期
     * @author whb
     * @date 2016年11月16日
     */
    public static List<Date> getDates(Date startTime, Date endTime) {
        List<Date> dates = new ArrayList<Date>();
        dates.add(startTime);
        Date nextDay = getNextDay(startTime);
        while (nextDay.before(endTime)) {
            dates.add(nextDay);
            nextDay = getNextDay(nextDay);
        }
        dates.add(endTime);
        return dates;
    }

    /**
     * 获取指定年指定月的第一天（公安行业每月第一天为上月的26日）
     *
     * @param year--年份
     * @param mon--月数
     * @return
     * @author hb.wang 2016-4-25
     */
    public static String getStartTime(String year, int mon) {
        int currYear = Integer.parseInt(year);
        if (mon <= 10) {
            if (mon == 1) {
                return currYear - 1 + "-" + "12-26";
            } else {
                return year + "-" + "0" + (mon - 1) + "-" + "26";
            }
        }
        return year + "-" + (mon - 1) + "-" + "26";
    }

    /**
     * 获取指定年指定月的最后一天（公安行业每月的最后一天为当月的25日）
     *
     * @param year--年份
     * @param mon--月数
     * @return
     * @author hb.wang 2016-4-25
     */
    public static String getEndTime(String year, int mon) {
        return year + "-" + (mon < 10 ? "0" + mon : mon) + "-" + 25;
    }

    /**
     * 判断指定年份是否是闰年
     *
     * @param year--年份
     * @return
     * @author hb.wang 2016-4-25
     */
    public static boolean isLeapYear(String year) {
        if (org.apache.commons.lang.StringUtils.isNotBlank(year)) {
            int tmp = Integer.parseInt(year);
            return (tmp % 4 == 0 && tmp % 100 != 0) || (tmp % 400 == 0);
        }
        return false;
    }

    /**
     * 获取上周当前的时间
     *
     * @param formatType
     * @return
     * @author hb.wang 2016-4-29
     */
    public static String getPrevWeekTime(String formatType) {
        Calendar tmp = Calendar.getInstance();
        tmp.add(Calendar.DAY_OF_YEAR, -7);
        Date tt = tmp.getTime();
        return formatDateByFormat(tt, formatType);
    }

    /**
     * 字符串转日期,根据字符串格式转相对应的日期格式
     *
     * @param dateStr--日期字符串
     * @return
     * @author hb.wang 2016-5-1
     */
    public static Date formatDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            try {
                dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                date = dateFormat.parse(dateStr);
            } catch (ParseException ea) {
                dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date = dateFormat.parse(dateStr);
                } catch (ParseException e1) {
                    dateFormat = new SimpleDateFormat("yyyyMMdd");
                    try {
                        date = dateFormat.parse(dateStr);
                    } catch (ParseException e2) {
                        try {
                            dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                            date = dateFormat.parse(dateStr);
                        } catch (ParseException ea1) {
                            dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                            try {
                                date = dateFormat.parse(dateStr);
                            } catch (ParseException e3) {
                                dateFormat = new SimpleDateFormat("yyyy-MM");
                                try {
                                    date = dateFormat.parse(dateStr);
                                } catch (ParseException e4) {
                                    dateFormat = new SimpleDateFormat("yyyyMM");
                                    try {
                                        date = dateFormat.parse(dateStr);
                                    } catch (ParseException e5) {
                                        dateFormat = new SimpleDateFormat("yyyy/MM");
                                        try {
                                            date = dateFormat.parse(dateStr);
                                        } catch (ParseException e6) {
                                            dateFormat = new SimpleDateFormat("yyyy");
                                            try {
                                                date = dateFormat.parse(dateStr);
                                            } catch (ParseException e7) {
                                                e7.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return date;
    }

    public static Date getBeginDate(Date beginDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getEndDate(Date endDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static String getBeginDate(String dateStr) {
        return formatDateByFormat(getBeginDate(formatDate(dateStr)), "yyyy-MM-dd HH:mm:ss");
    }

    public static String getEndDate(String dateStr) {
        return formatDateByFormat(getEndDate(formatDate(dateStr)), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * @param format
     * @return
     * @throws
     * @Description: 返回指定格式的当前月第一天日期
     * @author whb
     * @date 2016年9月2日
     */
    public static String getCurrentMonthBeginDate(String format) {
        return formatDateByFormat(getBeginDate(getFirstDayOfMonth(new Date())), format);
    }

    /**
     * @param format
     * @return
     * @throws
     * @Description: 返回指定格式的当前月最后一天日期
     * @author whb
     * @date 2016年9月2日
     */
    public static String getCurrentMonthEndDate(String format) {
        return formatDateByFormat(getEndDate(getLastDayOfMonth(new Date())), format);
    }

    /**
     * 计算某一年某一月每个周的开始时间和结束时间
     * 首先是获取该月的实际天数，然后从1号遍历直至该月最后一天，利用Calendar类找出该日是一周中的第几天,
     * （1）若该日是周日，则视为一周结束，并用该日期减去6找到该周的开始日期，若相减结果小于等于1，则说明该周起始日期应该是本月1号。
     * （2）若该日不是周日，且是该月最后一天，则应该在上面计算的基础上再加一周，视月末为该周的结束日,该周起始日期应为改日期减去该日在一周中的位置加2
     * 提示：Calendar 类中视周日为一周的开始,值是1
     *
     * @param year
     * @param mon
     * @return
     * @throws Exception
     * @author hb.wang 2016-12-7
     */
    public static Map<String, String> getYearMonWeekDate(String year, String mon) throws Exception {
        String date = year + "-" + mon;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date date1 = dateFormat.parse(date);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date1);
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Map<String, String> resultMap = new LinkedHashMap<String, String>();
        for (int i = 1; i <= days; i++) {
            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Date date2 = dateFormat1.parse(date + "-" + i);
            calendar.clear();
            calendar.setTime(date2);
            int k = new Integer(calendar.get(Calendar.DAY_OF_WEEK));
            // 若当天是周日
            if (k == 1) {
                if (i - 6 <= 1) {
                    resultMap.put(date + "-" + 1, date + "-" + i);
                } else {
                    resultMap.put(date + "-" + (i - 6), date + "-" + i);
                }
            }
            // 若是本月最后一天，且不是周日
            if (k != 1 && i == days) {
                resultMap.put(date + "-" + (i - k + 2), date + "-" + i);
            }
        }
        return resultMap;
    }

    /**
     * @param year
     * @param mon
     * @return
     * @throws Exception
     * @throws
     * @Description: 计算某一年某一月每个周的开始时间和结束时间（从上个月的26号到当月的25号如：2017-3-26到2017-4-25）
     * @author whb
     * @date 2017年4月11日
     */
    public static Map<String, String> getYearMonWeekDateForPolice(String year, String mon) throws Exception {

        String date = year + "-" + (Integer.parseInt(mon) - 1);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date date1 = dateFormat.parse(date);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date1);
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Map<String, String> resultMap = new LinkedHashMap<>();
        int tmp = 0;
        for (int i = 26; i <= days; i++) {
            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Date date2 = dateFormat1.parse(date + "-" + i);
            calendar.clear();
            calendar.setTime(date2);
            int k = new Integer(calendar.get(Calendar.DAY_OF_WEEK));
            // 若当天是周日
            if (k == 1 || days == 25) {
                if (i - tmp <= 1) {
                    dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                    date2 = dateFormat1.parse(date + "-" + i);
                    calendar.clear();
                    calendar.setTime(date2);
                    calendar.add(Calendar.DAY_OF_MONTH, -tmp - 1);
                    resultMap.put(formatDateByFormat(calendar.getTime(), "yyyy-MM-dd"), date + "-" + i);
                } else {
                    resultMap.put(date + "-" + (i - tmp), date + "-" + i);
                }
            }
            tmp++;
            if ((k == 1 && tmp > 0) || i == 25) {
                tmp = 0;
            }
            if (i == days && days != 25) {
                i = 1;
                days = 25;
                date = year + "-" + mon;
            }
        }
        return resultMap;
    }

    /**
     * @desc 计算两个时间的间隔（单位-秒）
     * @author whb
     * @date 2018/1/25
     */
    public static float getTimeSpace(long beginTime, long endTime) {
        return (endTime - beginTime) / 1000f;
    }

    /**
     * @Author: whb
     * @Descrition: 获取当月天数
     * @Date: 17:22 2018/3/24
     */
    public static int getCurMonthDays(String year, String month) {
        int days;
        int FebDay = 28;
        if (isLeapYear(year)) {
            FebDay = 29;
        }
        switch (month) {
            case "1":
            case "3":
            case "5":
            case "7":
            case "8":
            case "10":
            case "12":
                days = 31;
                break;
            case "4":
            case "6":
            case "9":
            case "11":
                days = 30;
                break;
            case "2":
                days = FebDay;
                break;
            default:
                days = 0;
                break;
        }
        return days;
    }

    /**
     * @Author: whb
     * @Descrition: 获取指定月份的第一天
     * @Date: 14:03 2018/5/11
     */
    public static Date getFirstDayByMonth(String month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), Integer.valueOf(month) - 1, 1, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 时间戳转日期字符串
     *
     * @param timeStamp--时间戳
     * @param format--日期格式
     * @return
     * @author whb 2018-6-26日
     */
    public static String timeStampToDateStr(String timeStamp, String format) {
        if (StringUtils.isBlank(timeStamp)) {
            return "";
        }
        if (StringUtils.isBlank(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(timeStamp)));
    }

    /**
     * 时间戳转日期
     *
     * @param timeStamp--时间戳
     * @param format--日期格式
     * @return
     * @author whb 2018-6-26日
     */
    public static Date timeStampToDate(String timeStamp, String format) {
        if (StringUtils.isBlank(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        String dateStr = timeStampToDateStr(timeStamp, format);
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        return DateUtils.getFormatDate(dateStr, format);
    }

    /**
     * 指定日期加上指定的秒数
     *
     * @param date--日期
     * @param seconds--秒数
     * @return
     * @author whb 2018-6-26日
     */
    public static Date addSeconds(Date date, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    /**
     * @desc 日期添加8小时
     * @author whb
     * @date 2018/7/13
     */
    public static Date add8Hour(Date date) {
        if (date == null) {
            return null;
        }
        //8小时时差
        long h = 28800000;
        return new Date(date.getTime() + h);
    }

    public static void main(String[] args) {
        String time = "2019-07-02 20:08:43";
        Date payTime = DateUtils.getFormatDate(time, "yyyy-MM-dd HH:mm:ss");
        System.out.println("payTime和当前时间比较：" + payTime.compareTo(new Date()));

        System.out.println("==========输出指定日期最后一天：" + DateUtils.getMonthEnd("2016-2-1", "yyyy-MM-dd") + "============");
        String str = "1498457677473";
        Long dateLong = Long.valueOf(str);
        System.out.println("longToDate:" + DateUtils.format(new Date(dateLong), "yyyy-MM-dd HH:mm:ss"));
        System.out.println("=============当年开始的第一天：" + DateUtils.format(DateUtils.getBeginDate(DateUtils.getCurrYearFirst()), "yyyy-MM-dd HH:mm:ss") + "======当年的最后一天：" + DateUtils.format(DateUtils.getEndDate(DateUtils.getCurrYearLast()), "yyyy-MM-dd HH:mm:ss"));
        String full_format = "yyyy-MM-dd\'T\'HH:mm:ss.SSS+0800";
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(full_format);
        System.out.println("东八区时间：" + sdf.format(now));

        System.err.println("加之前：" + timeStampToDateStr("1527868773000", "yyyy-MM-dd HH:mm:ss") + "，指定的日期加上秒数：" + addSeconds(timeStampToDate("1527868773000", "yyyy-MM-dd HH:mm:ss"), 22));
        System.out.println("时间戳转日期字符串：" + timeStampToDateStr("1527868773000", "yyyy-MM-dd HH:mm:ss") + ",时间戳转日期：" + timeStampToDate("1527868773000", "yyyy-MM-dd HH:mm:ss"));
        try {
            System.err.println("2017年10月开始时间：" + getStartTime("2017", 10));
            System.err.println("2017年10月结束时间：" + getEndTime("2017", 10));
            Map<String, Double> rmap = new HashMap<String, Double>();
            rmap.put("a", 1.1);
            rmap.put("b", 2.2);
            rmap.put("c", null);
            double c = rmap.get("c");
            double d = rmap.get("d");

            System.err.println("d:" + d + ",c:" + c);
            int count = 1;
            Map<String, String> resultMap = getYearMonWeekDate("2013", "1");
            for (String key : resultMap.keySet()) {
                System.err.println("第" + count + "周的开始时间：" + key + ",结束时间：" + resultMap.get(key));
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String deptName = "SZGBZD";
        String tmpName = "";
        for (int i = 0; i < deptName.length(); i++) {
            tmpName = tmpName + deptName.charAt(i);
            if (i != deptName.length() - 1) {
                tmpName += "\\n";
            }
        }
        System.err.println("tmpName:" + tmpName);
        System.err.println("开始时间：" + formatDateByFormat(getBeginDate(formatDate("2016/5/1")), "yyyy-MM-dd HH:mm:ss"));
        System.err.println("结束时间：" + formatDateByFormat(getEndDate(formatDate("2016/5/1")), "yyyy-MM-dd HH:mm:ss"));
        Calendar tmp = Calendar.getInstance();
        tmp.add(Calendar.DAY_OF_YEAR, -7);
        Date tt = tmp.getTime();
        System.err.println("上一周的当前时间：" + formatDateByFormat(tt, "yyyy-MM-dd HH:mm:ss"));
        Date t1 = tmp.getTime();
        tmp.add(Calendar.HOUR_OF_DAY, 1);
        Date t2 = tmp.getTime();
        System.err.println("时间t1:" + formatDateByFormat(t1, "yyyy-MM-dd HH:mm:ss") + ";时间t2:"
                + formatDateByFormat(t2, "yyyy-MM-dd HH:mm:ss") + ";t1在t2的前面" + (t1.before(t2) ? "是的" : "后面"));
        System.err.println("昨天的开始时间：" + getYesterdayDateStr());
        System.err.println("当天的开始时间：" + formatDateByFormat(getTodayStartTime(), "yyyy-MM-dd HH:mm:ss"));
        System.err.println("本年的第一天：" + formatDateByFormat(getCurrYearFirst(), "yyyy-MM-dd"));
        System.err.println("本年的最后一天：" + formatDateByFormat(getCurrYearLast(), "yyyy-MM-dd"));
        System.err.println("本月最后一天：" + formatDateByFormat(getLastDayOfMonth(new Date()), "yyyy-MM-dd"));
        System.err.println("本月第一天：" + formatDateByFormat(getFirstDayOfMonth(new Date()), "yyyy-MM-dd"));
        System.err.println("本周最后一天：" + formatDateByFormat(getLastDayOfWeek(new Date()), "yyyy-MM-dd"));
        System.err.println("本周第一天：" + formatDateByFormat(getFirstDayOfWeek(new Date()), "yyyy-MM-dd"));
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 3);
        System.err.println(betweenMinutes(new Date(), cal.getTime()));
        cal.add(Calendar.HOUR_OF_DAY, 2);
        System.err.println(formatDateByFormat(cal.getTime(), "yyyy-MM-dd HH:mm:ss"));
        cal.setTime(getFormatDate("2011-12", "yyyy-MM"));
        cal.add(Calendar.MONTH, -1);
        System.err.println(formatDateByFormat(cal.getTime(), "yyyy-MM"));
        System.err.println(getMonthBegin("201602", "yyyyMM"));
        System.out.println("" +
                "当前月的第一天" + getCurrentMonthBeginDate("yyyy-MM-dd HH:mm:ss"));
        System.out.println("当前月最后一天" + getCurrentMonthEndDate("yyyy-MM-dd HH:mm:ss"));
        System.out.println("获取指定月的第一天：：：：：" + getFirstDayByMonth("5"));
        try {
            Map<String, String> map = getYearMonWeekDateForPolice("2017", "4");
            for (Map.Entry<String, String> e : map.entrySet()) {
                System.out.println(e.getKey() + "================" + e.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
