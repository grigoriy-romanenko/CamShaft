package com.amcbridge.camshaft.service.profile;

import com.amcbridge.camshaft.model.CamShaft;
import com.amcbridge.camshaft.model.validators.CamShaftValidator;
import com.amcbridge.camshaft.service.profile.central.CentralProfileCalculator;
import com.amcbridge.camshaft.service.profile.central.CentralProfileCalculatorsContainer;
import com.amcbridge.camshaft.utils.Utils;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import org.apache.commons.math3.analysis.UnivariateFunction;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.amcbridge.camshaft.utils.Constants.*;
import static com.amcbridge.camshaft.utils.Utils.*;
import static javafx.scene.chart.XYChart.Data;
import static java.math.BigDecimal.*;

/**
 * @see com.amcbridge.camshaft.service.profile.ProfileService
 */
public class ProfileServiceImpl implements ProfileService {

    private CamShaftValidator camShaftValidator;
    private CentralProfileCalculatorsContainer calculatorsContainer;

    public ProfileServiceImpl(
            CamShaftValidator camShaftValidator, CentralProfileCalculatorsContainer calculatorsContainer) {
        Objects.requireNonNull(camShaftValidator);
        Objects.requireNonNull(calculatorsContainer);
        this.camShaftValidator = camShaftValidator;
        this.calculatorsContainer = calculatorsContainer;
    }

    /**
     * Calculates camshaft central(theoretical) profile using its geometry params and follower motion law.
     *
     * @param camShaft camshaft model
     */
    @Override
    public void calculateCentralProfile(CamShaft camShaft) {
        Map<String, BigDecimal> params = camShaft.getParams();
        List<Data<Number, Number>> motionLaw = camShaft.getMotionLaw();
        CentralProfileCalculator calculator = calculatorsContainer.getCalculator(camShaft.getFollowerType());
        motionLaw.sort((point1, point2) -> Double.compare(
                point1.getXValue().doubleValue(), point2.getXValue().doubleValue()));
        camShaftValidator.validate(camShaft);
        BigDecimal precision = params.get("precision");
        UnivariateFunction function = Utils.buildFunction(motionLaw);
        List<Data<Number, Number>> result = new ArrayList<>();
        BigDecimal x = X_AXIS_LOWER_BOUND;
        BigDecimal yMin = motionLaw.stream()
                .map(point -> valueOf(point.getYValue().doubleValue()))
                .min(BigDecimal::compareTo).get();
        while (x.compareTo(X_AXIS_UPPER_BOUND) <= 0) {
            BigDecimal y = valueOf(function.value(x.doubleValue())).subtract(yMin);
            BigDecimal nextY = valueOf(function.value(x.add(precision).remainder(X_AXIS_UPPER_BOUND).doubleValue()))
                    .subtract(yMin);
            BigDecimal r = calculator.calculateRi(y, params);
            BigDecimal f = calculator.buildFiCalculator(r, params).apply(y.compareTo(nextY) < 0, toRadians(x));
            BigDecimal profileX = r.multiply(cos(f));
            BigDecimal profileY = r.multiply(sin(f));
            result.add(new Data<>(profileX, profileY));
            x = x.add(precision);
        }
        camShaft.getCentralProfile().setAll(result);
    }

    /**
     * Calculates camshaft practical profile using its central(theoretical) profile.
     *
     * Uses Minkowski sum algorithm to offset polygon(central profile).
     *
     * @param camShaft camshaft model
     */
    @Override
    public void calculatePracticalProfile(CamShaft camShaft) {
        BigDecimal d = camShaft.getParams().get("d");
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate[] coordinates = camShaft.getCentralProfile().stream()
                .map(point -> new Coordinate(point.getXValue().doubleValue(), point.getYValue().doubleValue()))
                .toArray(Coordinate[]::new);
        CoordinateArraySequence coordinateArraySequence = new CoordinateArraySequence(coordinates);
        LinearRing linearRing = new LinearRing(coordinateArraySequence, geometryFactory);
        Polygon polygon = geometryFactory.createPolygon(linearRing);
        Geometry inset = polygon.buffer(d.negate().doubleValue() / 2, QUADRANT_SEGMENTS);
        List<Data<Number, Number>> result = Arrays.stream(inset.getCoordinates())
                .map(coordinate -> new Data<Number, Number>(coordinate.x, coordinate.y))
                .collect(Collectors.toList());
        camShaft.getPracticalProfile().setAll(result);
    }

}