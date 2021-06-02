package com.zd.datapanel.support;

import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.time.format.DateTimeFormatter;

/**
 * 常量
 * @author wang xiao
 * @date Created in 17:47 2021/1/26
 */
public final class Constants {
    private Constants () {}


    public static final String JD_FLAGSHIP_SHOP = "10001";

    public static final String JD_SELF_SHOP = "10002";

    public static final String TMALL_SHOP = "20001";

    public static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat();

    public static final  ParsePosition PARSE_POSITION = new ParsePosition(0);

    static {
        DECIMAL_FORMAT.setParseBigDecimal(true);
    }



}
