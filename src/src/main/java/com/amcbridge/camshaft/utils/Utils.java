package com.amcbridge.camshaft.utils;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;

import java.math.BigDecimal;
import java.util.List;

import static java.math.BigDecimal.ROUND_HALF_DOWN;
import static java.math.BigDecimal.valueOf;
import static java.math.MathContext.DECIMAL32;
import static javafx.scene.chart.XYChart.Data;

public class Utils {

    private Utils() {}

    public static BigDecimal cos(BigDecimal angle) {
        return valueOf(Math.cos(angle.doubleValue())).setScale(DECIMAL32.getPrecision(), ROUND_HALF_DOWN);
    }

    public static BigDecimal sin(BigDecimal angle) {
        return valueOf(Math.sin(angle.doubleValue())).setScale(DECIMAL32.getPrecision(), ROUND_HALF_DOWN);
    }

    public static BigDecimal acos(BigDecimal angle) {
        return valueOf(Math.acos(angle.doubleValue())).setScale(DECIMAL32.getPrecision(), ROUND_HALF_DOWN);
    }

    public static BigDecimal toRadians(BigDecimal angle) {
        return valueOf(Math.toRadians(angle.doubleValue())).setScale(DECIMAL32.getPrecision(), ROUND_HALF_DOWN);
    }

    public static BigDecimal sqrt(BigDecimal x) {
        return valueOf(Math.sqrt(x.doubleValue())).setScale(DECIMAL32.getPrecision(), ROUND_HALF_DOWN);
    }

    public static UnivariateFunction buildFunction(List<Data<Number, Number>> points) {
        double[] x = new double[points.size()];
        double[] y = new double[points.size()];
        for (int i = 0; i < points.size(); i++) {
            Data<Number, Number> point = points.get(i);
            x[i] = point.getXValue().doubleValue();
            y[i] = point.getYValue().doubleValue();
        }
        return new LinearInterpolator().interpolate(x, y);
    }

}