package com.saltside.birds.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by kunal on 7/5/2017.
 */
public class DateUtil {

    public static String FORMAT = "yyyy-MM-dd";
    private static final TimeZone utc = TimeZone.getTimeZone("UTC");
    private static final SimpleDateFormat formatter = new SimpleDateFormat(FORMAT);

    static {
        formatter.setTimeZone(utc);
    }

    public static String now() {
        return formatter.format(new Date()).toString();
    }
}
