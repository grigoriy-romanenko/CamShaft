package com.amcbridge.camshaft.service.motionlaw;

import com.amcbridge.camshaft.model.CamShaft;
import com.amcbridge.camshaft.model.MotionLawFragment;
import com.amcbridge.camshaft.model.validators.MotionLawFragmentValidator;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.amcbridge.camshaft.utils.Constants.X_AXIS_LOWER_BOUND;
import static com.amcbridge.camshaft.utils.Constants.X_AXIS_UPPER_BOUND;
import static java.math.BigDecimal.ROUND_HALF_DOWN;
import static java.math.MathContext.DECIMAL32;
import static javafx.scene.chart.XYChart.Data;
import static java.math.BigDecimal.valueOf;

/**
 * @see com.amcbridge.camshaft.service.motionlaw.MotionLawService
 */
public class MotionLawServiceImpl implements MotionLawService {

    private static final String VARIABLE_NAME = "x";
    private static final String INVALID_FORMAT_MSG = "Can not parse point from specified string: ";
    private static final String INPUT_OUT_OF_BOUNDS_MSG = "X-axis coordinate is out of bounds: ";

    private MotionLawFragmentValidator motionLawFragmentValidator;

    public MotionLawServiceImpl(MotionLawFragmentValidator fragmentValidator) {
        Objects.requireNonNull(fragmentValidator);
        this.motionLawFragmentValidator = fragmentValidator;
    }

    /**
     * Creates sequence of follower motion law points using specified fragment and sets it to camshaft.
     *
     * Uses exp4j to evaluate function.
     * Follow <a href="http://www.objecthunter.net/exp4j/apidocs/index.html"></a> /a> to see its API.
     * Existed follower motion law points that belong to fragment range will be replaced with new ones.
     *
     * @param camShaft camshaft model that stores follower motion law
     * @param fragment params to create sequence of follower motion law points
     *
     * @throws IllegalArgumentException if given fragment is invalid
     */
    @Override
    public void setMotionLawFragment(CamShaft camShaft, MotionLawFragment fragment) {
        motionLawFragmentValidator.validate(fragment);
        Expression expression = new ExpressionBuilder(fragment.getFunction()).variable(VARIABLE_NAME).build();
        List<Data<Number, Number>> motionLawFragment = new ArrayList<>();
        BigDecimal x = fragment.getStart();
        while (x.compareTo(fragment.getEnd()) <= 0) {
            expression.setVariable(VARIABLE_NAME, x.doubleValue());
            BigDecimal y = valueOf(expression.evaluate())
                    .setScale(DECIMAL32.getPrecision(), ROUND_HALF_DOWN);
            motionLawFragment.add(new Data<>(x, y));
            x = x.add(fragment.getStep());
        }
        List<Data<Number, Number>> motionLaw = camShaft.getMotionLaw();
        motionLaw.removeIf(point ->
                valueOf(point.getXValue().doubleValue()).compareTo(fragment.getStart()) >= 0
                        && valueOf(point.getXValue().doubleValue()).compareTo(fragment.getEnd()) <= 0);
        motionLaw.addAll(motionLawFragment);
        motionLaw.sort((point1, point2) ->
                Double.compare(point1.getXValue().doubleValue(), point2.getXValue().doubleValue()));
    }

    /**
     * Parses follower motion law points from specified lines stream and sets it to camshaft model.
     *
     * On successful read existed camshaft follower motion law will be replaced with read one.
     * Each line should contain point coordinates in specified format: "(\\d+) (-?\\d+)"
     *
     * @param camShaft camshaft model that stores follower motion law
     * @param lines lines stream
     *
     * @throws IllegalArgumentException if line has invalid format or x-axis coordinate is out of [0;360] range
     */
    @Override
    public void readMotionLaw(CamShaft camShaft, Stream<String> lines) {
        List<Data<Number, Number>> data = new ArrayList<>();
        lines.forEach(line -> {
            if (!line.matches("(\\d+) (-?\\d+)")) {
                throw new IllegalArgumentException(INVALID_FORMAT_MSG + line);
            }
            String[] coordinates = line.split(" ");
            BigDecimal x = new BigDecimal(coordinates[0]);
            if (x.compareTo(X_AXIS_LOWER_BOUND) < 0 || x.compareTo(X_AXIS_UPPER_BOUND) > 0) {
                throw new IllegalArgumentException(INPUT_OUT_OF_BOUNDS_MSG + x);
            }
            BigDecimal y = new BigDecimal(coordinates[1]);
            data.add(new Data<>(x, y));
        });
        camShaft.getMotionLaw().setAll(data);
    }

}
