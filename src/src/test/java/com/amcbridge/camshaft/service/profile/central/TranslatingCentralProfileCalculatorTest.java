package com.amcbridge.camshaft.service.profile.central;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static java.math.BigDecimal.*;

public class TranslatingCentralProfileCalculatorTest {

    private Map<String, BigDecimal> params;
    private CentralProfileCalculator calculator;

    @Before
    public void before() {
        params = new HashMap<>();
        calculator = new TranslatingCentralProfileCalculator();
    }

    @Test
    public void testCalculateRi() {
        params.put("Rmin", valueOf(5));
        params.put("X", valueOf(3));
        BigDecimal s = valueOf(0);

        assertTrue(valueOf(5).compareTo(calculator.calculateRi(s, params)) == 0);
    }

}