package com.amcbridge.camshaft.service.profile.central;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.BiFunction;

public interface CentralProfileCalculator {

    /**
     * Calculates profile radius.
     *
     * @param s follower transition value
     * @param params camshaft geometry params
     * @return profile radius
     */
    BigDecimal calculateRi(BigDecimal s, Map<String, BigDecimal> params);

    /**
     * Builds function to calculate profile angle.
     *
     * @param r profile radius
     * @param params camshaft geometry params
     * @return function to calculate profile angle
     */
    BiFunction<Boolean, BigDecimal, BigDecimal> buildFiCalculator(BigDecimal r, Map<String, BigDecimal> params);

}