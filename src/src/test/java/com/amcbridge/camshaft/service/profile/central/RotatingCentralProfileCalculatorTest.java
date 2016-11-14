package com.amcbridge.camshaft.service.profile.central;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static java.math.BigDecimal.valueOf;
import static org.junit.Assert.*;

public class RotatingCentralProfileCalculatorTest {

    private Map<String, BigDecimal> params;
    private CentralProfileCalculator calculator;

    @Before
    public void before() {
        params = new HashMap<>();
        calculator = new RotatingCentralProfileCalculator();
    }

    @Test
    public void testCalculateRi() {
        params.put("Rmin", valueOf(1));
        params.put("L", valueOf(4));
        params.put("X", valueOf(5));
        params.put("Y", valueOf(0));
        BigDecimal s = valueOf(0);

        assertTrue(valueOf(1).compareTo(calculator.calculateRi(s, params)) == 0);
    }

}