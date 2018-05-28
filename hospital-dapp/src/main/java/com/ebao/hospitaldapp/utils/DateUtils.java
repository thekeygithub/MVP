package com.ebao.hospitaldapp.utils;

import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static final DateTimeFormatter YEAR_MONTH_DAY_24H_M_S = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter YEAR_MONTH_DAY_12H_M_S = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
    public static final DateTimeFormatter YEAR_MONTH_DAY = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter YEAR_MONTH = DateTimeFormatter.ofPattern("yyyy-MM");
}
