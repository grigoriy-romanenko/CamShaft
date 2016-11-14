package com.amcbridge.camshaft.service.profile.central;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.BiFunction;

import static com.amcbridge.camshaft.utils.Utils.*;
import static java.math.BigDecimal.*;

public class TranslatingCentralProfileCalculator implements CentralProfileCalculator {

    @Override
    public BigDecimal calculateRi(BigDecimal s, Map<String, BigDecimal> params) {
        BigDecimal r0 = params.get("Rmin");
        BigDecimal x = params.get("X");

        BigDecimal s0 = sqrt(r0.multiply(r0).subtract(x.multiply(x)));
        return sqrt(x.multiply(x).add(s.add(s0).pow(2)));
    }

    @Override
    public BiFunction<Boolean, BigDecimal, BigDecimal> buildFiCalculator(BigDecimal r, Map<String, BigDecimal> params) {
        BigDecimal r0 = params.get("Rmin");
        BigDecimal x = params.get("X");

        BigDecimal dA = acos(x.divide(r, ROUND_HALF_DOWN)).subtract(acos(x.divide(r0, ROUND_HALF_DOWN)));
        BigDecimal a = x.signum() == 1 ? dA.negate() : dA;
        return (isRisingPhase, w) -> isRisingPhase ? w.add(a) : w.subtract(a);
    }

}