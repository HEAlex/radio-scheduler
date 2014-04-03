package ua.edu.sumdu.util;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import ua.edu.sumdu.domain.Day;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: flash
 * Date: 21.01.12
 */
public class DateUtils {

    static String[] weekdays = new String[]{"понедельник", "вторник", "среда",
            "четверг", "пятница", "суббота", "воскресенье"};
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");

    public static List<Day> getWeekDays(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getWeekStartDate(date));
        List<Day> days = new ArrayList<Day>(weekdays.length);
        for (String dayName : weekdays) {
            Day day = new Day();
            day.setName(dayName);
            day.setDate(cal.getTime());
            days.add(day);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return days;
    }

    public static int getDayNumber(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return (cal.get(Calendar.DAY_OF_WEEK) + 7 - Calendar.MONDAY) % 7;
    }

    public static Date getWeekStartDate() {
        return getWeekStartDate(new Date());
    }

    public static Date getWeekStartDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_WEEK, -getDayNumber(date));
        return clearDate(cal.getTime());
    }

    public static Date getWeekEndDate() {
        return getWeekEndDate(new Date());
    }

    public static Date getWeekEndDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_WEEK, 7 - getDayNumber(date));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    public static String now(String dateFormat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());
    }

    public static Date clearDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        return cal.getTime();
    }

    public static int getDayDifference(Date date1, Date date2) {
        return (int) Math.abs(date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000);
    }

    public static SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public static void setDateFormat(SimpleDateFormat dateFormat) {
        DateUtils.dateFormat = dateFormat;
    }
}
