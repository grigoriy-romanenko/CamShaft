package com.amcbridge.camshaft.service.profile.central;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.BiFunction;

import static com.amcbridge.camshaft.utils.Utils.*;
import static java.math.BigDecimal.*;

public class RotatingCentralProfileCalculator implements CentralProfileCalculator {

    @Override
    public BigDecimal calculateRi(BigDecimal s, Map<String, BigDecimal> params) {
        BigDecimal r0 = params.get("Rmin");
        BigDecimal l = params.get("L");
        BigDecimal x = params.get("X");
        BigDecimal y = params.get("Y");

        BigDecimal e = calculateE(x, y);
        BigDecimal f = acos(e.multiply(e).add(l.multiply(l)).subtract(r0.multiply(r0)).divide(
                valueOf(2).multiply(e).multiply(l), ROUND_HALF_DOWN));
        return sqrt(e.multiply(e).add(l.multiply(l)).subtract(
                valueOf(2).multiply(e).multiply(l).multiply(cos(f.add(s.divide(l, ROUND_HALF_DOWN))))));
    }

    @Override
    public BiFunction<Boolean, BigDecimal, BigDecimal> buildFiCalculator(BigDecimal r, Map<String, BigDecimal> params) {
        BigDecimal r0 = params.get("Rmin");
        BigDecimal l = params.get("L");
        BigDecimal x = params.get("X");
        BigDecimal y = params.get("Y");

        BigDecimal e = calculateE(x, y);
        BigDecimal f0 = acos(r0.multiply(r0).add(e.multiply(e)).subtract(l.multiply(l))
                .divide(valueOf(2).multiply(r0).multiply(e), ROUND_HALF_DOWN));
        BigDecimal fi = acos(r.multiply(r).add(e.multiply(e)).subtract(l.multiply(l)).divide(
                valueOf(2).multiply(r).multiply(e), ROUND_HALF_DOWN));
        BigDecimal a = f0.subtract(fi);
        return (isRisingPhase, w) -> isRisingPhase ? w.add(a) : w.subtract(a);
    }

    private BigDecimal calculateE(BigDecimal x, BigDecimal y) {
        return sqrt(x.multiply(x).add(y.multiply(y)));
    }

}