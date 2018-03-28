package com.txh.im.utils;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by jiajia on 2017/3/29.
 */

public class DateChatUtils {
    private static final long INTERVAL_IN_MILLISECONDS = 30000L;

    public DateChatUtils() {
    }

    public static String getTimestampString(Date date) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy/MM/dd HH:mm");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yy/MM/dd");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("HH");
        Date datenow = new Date();
        if (simpleDateFormat1.format(date).equals(simpleDateFormat1.format(datenow))) {
            if (simpleDateFormat3.format(date).equals(simpleDateFormat3.format(datenow))) {
                return simpleDateFormat2.format(date);
            }
            return simpleDateFormat2.format(date);
        } else {
            return simpleDateFormat1.format(date);
        }
    }

    public static boolean isCloseEnough(long var0, long var2) {
        long var4 = var0 - var2;
        if (var4 < 0L) {
            var4 = -var4;
        }

        return var4 < 30000L;
    }

    public static String getTimestampStr() {
        return Long.toString(System.currentTimeMillis());
    }
}
