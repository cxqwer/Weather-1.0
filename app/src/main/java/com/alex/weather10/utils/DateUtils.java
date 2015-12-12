package com.alex.weather10.utils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class DateUtils {

    public static Calendar getCalendarDate(long dt) {

        Calendar tCalendar = Calendar.getInstance();
        tCalendar.setTime(new Date(dt*1000L));

        return tCalendar;
    }

    public static String getDate(long dt) {
        //long unixSeconds = Integer.parseInt(dt);
        Date date = new Date(dt*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")); // give a timezone reference for formating (see comment at the bottom
        return (sdf.format(date));

    }

    public static int getDay(long dt) {
        Calendar cal = getCalendarDate(dt);
        return(cal.get(Calendar.DAY_OF_MONTH));
    }

    public static int getDayOfWeek(long dt) {
        Calendar cal = getCalendarDate(dt);
        return(cal.get(Calendar.DAY_OF_WEEK));
    }

    public static String getMonth(long dt) {
        Calendar cal = getCalendarDate(dt);
        int num = cal.get(Calendar.MONTH);

        String month = "";

        String[] months = {
                "января", "февраля", "марта", "апреля", "мая", "июня",
                "июля", "августа", "сентября", "октября", "ноября", "декабря"};

        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }

        return month;
    }

    public static int getYear(long dt) {
        Calendar cal = getCalendarDate(dt);
        return(cal.get(Calendar.YEAR));
    }


}
