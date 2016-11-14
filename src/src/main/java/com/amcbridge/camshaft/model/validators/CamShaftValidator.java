package com.amcbridge.camshaft.model.validators;

import com.amcbridge.camshaft.model.CamShaft;
import com.amcbridge.camshaft.exceptions.InvalidInputParamException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.amcbridge.camshaft.utils.Constants.*;
import static javafx.scene.chart.XYChart.Data;
import static java.math.BigDecimal.*;

public class CamShaftValidator {

    private static final String ILLEGAL_START_MSG = "First point should has 0 x-axis value";
    private static final String ILLEGAL_END_MSG = "Last point should has 360 x-axis value";
    private static final String END_DOES_NOT_MATCHES_START_MSG = "First and last point should has same y-axis value";
    private static final String NOT_ENOUGH_POINTS_MSG = "Motion law should has at least 2 points";
    private static final String ILLEGAL_PRECISION_MSG =
            "Precision should be in [" + PRECISION_LOWER_BOUND + "; " + PRECISION_UPPER_BOUND + "] range";
    private static final String ILLEGAL_ROLLER_D_MSG = "d should be in (0; Rmin) range";
    private static final String ILLEGAL_R_MIN_MSG = "R min should be positive";

    public void validate(CamShaft camShaft) {
        validateInputParams(camShaft.getParams());
        validateMotionLaw(camShaft.getMotionLaw());
    }

    private void validateInputParams(Map<String, BigDecimal> params) {
        BigDecimal precision = params.get("precision");
        BigDecimal r0 = params.get("Rmin");
        BigDecimal d = params.get("d");
        if (precision.compareTo(PRECISION_LOWER_BOUND) < 0 || precision.compareTo(PRECISION_UPPER_BOUND) > 0) {
            throw new InvalidInputParamException(ILLEGAL_PRECISION_MSG);
        }
        if (r0.compareTo(ZERO) <= 0) {
            throw new InvalidInputParamException(ILLEGAL_R_MIN_MSG);
        }
        if (d.compareTo(ZERO) <= 0 || d.compareTo(r0) >= 0) {
            throw new InvalidInputParamException(ILLEGAL_ROLLER_D_MSG);
        }
    }

    private void validateMotionLaw(List<Data<Number, Number>> motionLaw) {
        if (motionLaw.size() < 2) {
            throw new IllegalArgumentException(NOT_ENOUGH_POINTS_MSG);
        }
        Data<Number, Number> firstPoint = motionLaw.get(0);
        Data<Number, Number> lastPoint = motionLaw.get(motionLaw.size() - 1);
        if (BigDecimal.valueOf(firstPoint.getXValue().doubleValue()).compareTo(X_AXIS_LOWER_BOUND) != 0) {
            throw new IllegalArgumentException(ILLEGAL_START_MSG);
        }
        if (BigDecimal.valueOf(lastPoint.getXValue().doubleValue()).compareTo(X_AXIS_UPPER_BOUND) != 0) {
            throw new IllegalArgumentException(ILLEGAL_END_MSG);
        }
        if (BigDecimal.valueOf(firstPoint.getYValue().doubleValue())
                .compareTo(BigDecimal.valueOf(lastPoint.getYValue().doubleValue())) != 0) {
            throw new IllegalArgumentException(END_DOES_NOT_MATCHES_START_MSG);
        }
    }

}