package com.amcbridge.camshaft.utils;

import java.math.BigDecimal;

public class Constants {

    public static final BigDecimal X_AXIS_LOWER_BOUND = BigDecimal.ZERO;
    public static final BigDecimal X_AXIS_UPPER_BOUND = BigDecimal.valueOf(360);
    public static final BigDecimal PRECISION_LOWER_BOUND = BigDecimal.valueOf(0.1);
    public static final BigDecimal PRECISION_UPPER_BOUND = BigDecimal.ONE;
    public static final int QUADRANT_SEGMENTS = 12;

    public static final String DOUBLE_PATTERN = "-?((\\d*)|(\\d+\\.\\d*))";
    public static final String POSITIVE_DOUBLE_PATTERN = "(\\d*)|(\\d+\\.\\d*)";

    private Constants() {}

}
