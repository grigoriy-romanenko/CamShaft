package com.amcbridge.camshaft.model;

import java.math.BigDecimal;

public class MotionLawFragment {

    private String function;
    private BigDecimal step;
    private BigDecimal start;
    private BigDecimal end;

    public MotionLawFragment(String function, BigDecimal step, BigDecimal start, BigDecimal end) {
        this.function = function;
        this.step = step;
        this.start = start;
        this.end = end;
    }

    public String getFunction() {
        return function;
    }

    public BigDecimal getStep() {
        return step;
    }

    public BigDecimal getStart() {
        return start;
    }

    public BigDecimal getEnd() {
        return end;
    }

}