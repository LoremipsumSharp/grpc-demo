package io.loremipsum.grpc.demo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static final String YYYY_MM_DD_HH_MM_SS_fff = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    private static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }
}
