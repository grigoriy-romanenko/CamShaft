package com.amcbridge.camshaft.model.validators;

import com.amcbridge.camshaft.model.MotionLawFragment;

import java.math.BigDecimal;

import static com.amcbridge.camshaft.utils.Constants.*;

public class MotionLawFragmentValidator {

    private static final String EMPTY_FUNCTION_MSG = "Function should be entered";
    private static final String STEP_IS_OUT_OF_BOUNDS_MSG = "Step should be in range (0, 360) exclusive";
    private static final String NEGATIVE_START_MSG = "Start should be non-negative";
    private static final String END_OVERLOAD_MSG = "End should be less or equal to 360";
    private static final String START_IS_GREATER_THAN_END_MSG = "Start should be less than end";

    public void validate(MotionLawFragment fragment) {
        validateFunction(fragment.getFunction());
        validateStep(fragment.getStep());
        validateStartAndEnd(fragment.getStart(), fragment.getEnd());
    }

    private void validateFunction(String function) {
        if (function == null || function.trim().isEmpty()) {
            throw new IllegalArgumentException(EMPTY_FUNCTION_MSG);
        }
    }

    private void validateStep(BigDecimal step) {
        if (step.compareTo(X_AXIS_LOWER_BOUND) <= 0 || step.compareTo(X_AXIS_UPPER_BOUND) >= 0) {
            throw new IllegalArgumentException(STEP_IS_OUT_OF_BOUNDS_MSG);
        }
    }

    private void validateStartAndEnd(BigDecimal start, BigDecimal end) {
        if (start.compareTo(X_AXIS_LOWER_BOUND) < 0) {
            throw new IllegalArgumentException(NEGATIVE_START_MSG);
        }
        if (end.compareTo(X_AXIS_UPPER_BOUND) > 0) {
            throw new IllegalArgumentException(END_OVERLOAD_MSG);
        }
        if (start.compareTo(end) >= 0) {
            throw new IllegalArgumentException(START_IS_GREATER_THAN_END_MSG);
        }
    }

}