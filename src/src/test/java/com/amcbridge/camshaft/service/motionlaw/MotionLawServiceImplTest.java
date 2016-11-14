package com.amcbridge.camshaft.service.motionlaw;

import com.amcbridge.camshaft.model.CamShaft;
import com.amcbridge.camshaft.model.MotionLawFragment;
import com.amcbridge.camshaft.model.validators.MotionLawFragmentValidator;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static javafx.scene.chart.XYChart.Data;
import static java.math.BigDecimal.*;

public class MotionLawServiceImplTest {

    private CamShaft camShaft;
    private MotionLawService motionLawService;

    @Before
    public void before() {
        camShaft = new CamShaft();
        motionLawService = new MotionLawServiceImpl(new MotionLawFragmentValidator());
    }

    @Test
    public void testSetMotionLawFragment() {
        Map<String, BigDecimal> params = new HashMap<String, BigDecimal>() {{ put("precision", BigDecimal.ONE); }};
        camShaft.setParams(params);
        camShaft.getMotionLaw().setAll(Arrays.asList(
                new Data<>(0, 0), new Data<>(180, 50), new Data<>(360, 0)
        ));
        BigDecimal step = valueOf(10);
        BigDecimal start = valueOf(180);
        BigDecimal end = valueOf(190);
        MotionLawFragment fragment = new MotionLawFragment("x+1", step, start, end);
        List<Data<BigDecimal, BigDecimal>> expected = Arrays.asList(
                new Data<>(valueOf(0), valueOf(0)),
                new Data<>(valueOf(180), valueOf(181)),
                new Data<>(valueOf(190), valueOf(191)),
                new Data<>(valueOf(360), valueOf(0))
        );

        motionLawService.setMotionLawFragment(camShaft, fragment);

        for (int i = 0; i < expected.size(); i++) {
            BigDecimal xExpected = expected.get(i).getXValue();
            BigDecimal yExpected = expected.get(i).getYValue();
            BigDecimal xActual = valueOf(camShaft.getMotionLaw().get(i).getXValue().doubleValue());
            BigDecimal yActual = valueOf(camShaft.getMotionLaw().get(i).getYValue().doubleValue());
            assertTrue(xExpected.compareTo(xActual) == 0 && yExpected.compareTo(yActual) == 0);
        }
    }

    @Test
    public void testReadMotionLaw() {
        Stream<String> lines = Stream.of("1 1", "2 2");
        List<Pair<BigDecimal, BigDecimal>> expected = Arrays.asList(
                new Pair<>(valueOf(1), valueOf(1)), new Pair<>(valueOf(2), valueOf(2))
        );

        motionLawService.readMotionLaw(camShaft, lines);

        List<Pair<BigDecimal, BigDecimal>> actual = camShaft.getMotionLaw().stream()
                .map(point -> new Pair<>(valueOf(point.getXValue().intValue()), valueOf(point.getYValue().intValue())))
                .collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadMotionLawWithInvalidLineFormat() {
        Stream<String> lines = Stream.of("1 1", "22");
        motionLawService.readMotionLaw(camShaft, lines);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadMotionLawWithInvalidXCoordinate() {
        Stream<String> lines = Stream.of("1 1", "-2 2");
        motionLawService.readMotionLaw(camShaft, lines);
    }

}