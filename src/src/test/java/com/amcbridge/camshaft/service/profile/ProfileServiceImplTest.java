package com.amcbridge.camshaft.service.profile;

import com.amcbridge.camshaft.model.CamShaft;
import com.amcbridge.camshaft.model.validators.CamShaftValidator;
import com.amcbridge.camshaft.service.profile.central.CentralProfileCalculator;
import com.amcbridge.camshaft.service.profile.central.CentralProfileCalculatorsContainer;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static javafx.scene.chart.XYChart.Data;

public class ProfileServiceImplTest {

    private CamShaft camShaft;
    private ProfileService profileService;

    @Before
    public void before() {
        camShaft = new CamShaft();
        Map<String, BigDecimal> params = new HashMap<>();
        params.put("Rmin", BigDecimal.valueOf(100));
        params.put("d", BigDecimal.valueOf(20));
        params.put("precision", BigDecimal.ONE);
        camShaft.setParams(params);
        camShaft.getMotionLaw().setAll(Arrays.asList(new Data<>(0, 0), new Data<>(360, 0)));
        CentralProfileCalculator calculator = mock(CentralProfileCalculator.class);
        when(calculator.calculateRi(any(), any())).thenReturn(BigDecimal.valueOf(100));
        when(calculator.buildFiCalculator(any(), any())).thenReturn((isRisingPhase, x) -> x);
        CentralProfileCalculatorsContainer calculatorsContainer = mock(CentralProfileCalculatorsContainer.class);
        when(calculatorsContainer.getCalculator(any())).thenReturn(calculator);
        profileService = new ProfileServiceImpl(new CamShaftValidator(), calculatorsContainer);
    }

    @Test
    public void testCalculateCentralProfile() {
        List<Pair<Double, Double>> expected = Arrays.asList(
                new Pair<>(100d, 0d),
                new Pair<>(0d, 100d),
                new Pair<>(-100d, 0d),
                new Pair<>(0d, -100d),
                new Pair<>(100d, 0d));

        profileService.calculateCentralProfile(camShaft);

        List<Pair<Double, Double>> actual = camShaft.getCentralProfile().stream()
                .filter(point -> point.getXValue().doubleValue() == 0d || point.getYValue().doubleValue() == 0d)
                .map(point -> new Pair<>(point.getXValue().doubleValue(), point.getYValue().doubleValue()))
                .collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    @Test
    public void testCalculatePracticalProfile() {
        List<Pair<Double, Double>> expected = Arrays.asList(
                new Pair<>(90d, 0d),
                new Pair<>(0d, -90d),
                new Pair<>(-90d, 0d),
                new Pair<>(0d, 90d),
                new Pair<>(90d, 0d));

        profileService.calculateCentralProfile(camShaft);
        profileService.calculatePracticalProfile(camShaft);

        List<Pair<Double, Double>> actual = camShaft.getPracticalProfile().stream()
                .filter(point -> point.getXValue().doubleValue() == 0d || point.getYValue().doubleValue() == 0d)
                .map(point -> new Pair<>(point.getXValue().doubleValue(), point.getYValue().doubleValue()))
                .collect(Collectors.toList());

        double delta = 0.1;
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getKey(), actual.get(i).getKey(), delta);
            assertEquals(expected.get(i).getValue(), actual.get(i).getValue(), delta);
        }
    }

}