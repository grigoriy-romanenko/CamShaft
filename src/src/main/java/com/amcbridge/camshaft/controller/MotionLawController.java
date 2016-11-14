package com.amcbridge.camshaft.controller;

import com.amcbridge.camshaft.controller.dialogs.MotionLawFragmentDialog;
import com.amcbridge.camshaft.controller.dialogs.MovePointDialog;
import com.amcbridge.camshaft.model.CamShaft;
import com.amcbridge.camshaft.exceptions.InvalidInputParamException;
import com.amcbridge.camshaft.model.MotionLawFragment;
import com.amcbridge.camshaft.service.profile.ProfileService;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import org.apache.log4j.Logger;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.Optional;
import java.util.stream.Stream;

import static com.amcbridge.camshaft.utils.Constants.*;
import static javafx.scene.chart.XYChart.Data;
import static java.math.BigDecimal.*;

public class MotionLawController {

    private static final Logger LOGGER = Logger.getLogger(MotionLawController.class);

    private static final String INVALID_INPUT_FILE = "Can not read file content";
    private static final String INVALID_MOTION_LAW_FRAGMENT_PARAMS = "Can not create motion law using function";
    private static final String INVALID_MOTION_LAW = "Invalid motion law";
    private static final String INVALID_GEOMETRY_PARAM = "Invalid geometry param";
    private static final String X_OUT_OF_BOUNDS = "X value should be in [0 - 360] range";

    @FXML
    private LineChart<Number, Number> chart;
    @FXML
    private Button openButton;
    @FXML
    private Button functionButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button prevSceneButton;
    @FXML
    private Button nextSceneButton;

    private Context context;
    private MovePointDialog movePointDialog;

    public MotionLawController(Context context, MovePointDialog movePointDialog) {
        this.context = context;
        this.movePointDialog = movePointDialog;
    }

    /**
     * invokes on FXMLLoader.load()
     */
    @FXML
    private void initialize() {
        setAddPointEventHandler();
        chart.getData().add(new XYChart.Series<>(context.getCamShaft().getMotionLaw()));
        context.getCamShaft().getMotionLaw().addListener(new MotionLawChangeListener());
        initOpenButton();
        initFunctionButton();
        clearButton.setOnAction(event -> context.getCamShaft().getMotionLaw().clear());
        prevSceneButton.setOnAction(event -> context.getStage().setScene(context.getGeometryScene()));
        initNextSceneButton();
    }

    private void initNextSceneButton() {
        nextSceneButton.setOnAction(event -> {
            try {
                CamShaft camShaft = context.getCamShaft();
                ProfileService profileService = context.getProfileService();
                profileService.calculateCentralProfile(camShaft);
                profileService.calculatePracticalProfile(camShaft);
                context.getStage().setScene(context.getProfileScene());
            }
            catch (InvalidInputParamException e) {
                LOGGER.warn(INVALID_GEOMETRY_PARAM, e);
                new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
                context.getStage().setScene(context.getGeometryScene());
            }
            catch (IllegalArgumentException e) {
                LOGGER.warn(INVALID_MOTION_LAW, e);
                new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            }
        });
    }

    private void initOpenButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("txt files", "*.txt"));
        openButton.setOnMouseClicked(event -> {
            File file = fileChooser.showOpenDialog(context.getStage());
            if (file == null) return;
            try (Stream<String> lines = Files.lines(file.toPath())) {
                context.getMotionLawService().readMotionLaw(context.getCamShaft(), lines);
            }
            catch (Exception e) {
                LOGGER.warn(INVALID_INPUT_FILE, e);
                new Alert(Alert.AlertType.ERROR, INVALID_INPUT_FILE).showAndWait();
            }
        });
    }

    private void initFunctionButton() {
        Dialog<MotionLawFragment> motionLawFragmentDialog = new MotionLawFragmentDialog();
        functionButton.setOnAction(event -> {
            try {
                Optional<MotionLawFragment> result = motionLawFragmentDialog.showAndWait();
                result.ifPresent(params -> context.getMotionLawService()
                        .setMotionLawFragment(context.getCamShaft(), params));
            }
            catch (IllegalArgumentException e) {
                LOGGER.warn(INVALID_MOTION_LAW_FRAGMENT_PARAMS, e);
                new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            }
            finally {
                motionLawFragmentDialog.setResult(null);
            }
        });
    }

    private void setAddPointEventHandler() {
        chart.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                Pair<BigDecimal, BigDecimal> coordinates = getClickCoordinates(mouseEvent);
                if (isXInvalid(coordinates.getKey())) return;
                ContextMenu addPointMenu = new ContextMenu();
                MenuItem menuItem = new MenuItem("Add");
                Data<Number, Number> point = new Data<>(coordinates.getKey(), coordinates.getValue());
                menuItem.setOnAction(event -> context.getCamShaft().getMotionLaw().add(point));
                addPointMenu.getItems().add(menuItem);
                addPointMenu.show(context.getStage(), mouseEvent.getScreenX(), mouseEvent.getScreenY());
            }
        });
    }

    private void setContextMenuForExistingPoint(Data<Number, Number> data) {
        Node node = data.getNode();
        node.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                ContextMenu contextMenu = new ContextMenu();
                MenuItem deleteMenuItem = new MenuItem("Delete");
                deleteMenuItem.setOnAction(menuItemEvent -> context.getCamShaft().getMotionLaw().remove(data));
                contextMenu.getItems().addAll(createMovePointMenuItem(data), deleteMenuItem);
                contextMenu.show(node, event.getScreenX(), event.getScreenY());
                event.consume();
            }
        });
    }

    private MenuItem createMovePointMenuItem(Data<Number, Number> data) {
        MenuItem moveMenuItem = new MenuItem("Move");
        moveMenuItem.setOnAction(event -> {
            movePointDialog.setText(String.valueOf(data.getXValue().doubleValue()),
                    String.valueOf(data.getYValue().doubleValue()));
            Optional<Pair<BigDecimal, BigDecimal>> result = movePointDialog.showAndWait();
            result.ifPresent(pair -> {
                if (isXInvalid(pair.getKey())) {
                    new Alert(Alert.AlertType.ERROR, X_OUT_OF_BOUNDS).showAndWait();
                    return;
                }
                data.setXValue(pair.getKey());
                data.setYValue(pair.getValue());
            });
            movePointDialog.setResult(null);
        });
        return moveMenuItem;
    }

    private boolean isXInvalid(BigDecimal x) {
        return x.compareTo(X_AXIS_LOWER_BOUND) < 0 || x.compareTo(X_AXIS_UPPER_BOUND) > 0;
    }

    private Pair<BigDecimal, BigDecimal> getClickCoordinates(MouseEvent event) {
        Point2D pointInScene = new Point2D(event.getSceneX(), event.getSceneY());
        double xAxisLocal = chart.getXAxis().sceneToLocal(pointInScene).getX();
        double yAxisLocal = chart.getYAxis().sceneToLocal(pointInScene).getY();
        BigDecimal x = valueOf(chart.getXAxis().getValueForDisplay(xAxisLocal).doubleValue());
        BigDecimal y = valueOf(chart.getYAxis().getValueForDisplay(yAxisLocal).doubleValue());
        return new Pair<>(x, y);
    }

    private class MotionLawChangeListener implements ListChangeListener<Data<Number, Number>> {

        @Override
        public void onChanged(Change<? extends Data<Number, Number>> change) {
            while (change.next()) {
                change.getAddedSubList().forEach(data -> {
                    setContextMenuForExistingPoint(data);
                    Node node = data.getNode();
                    node.setCursor(Cursor.HAND);
                    node.setOnMouseDragged(event -> {
                        Pair<BigDecimal, BigDecimal> coordinates = getClickCoordinates(event);
                        if (isXInvalid(coordinates.getKey())) return;
                        data.setXValue(coordinates.getKey());
                        data.setYValue(coordinates.getValue());
                    });
                });
            }
        }

    }

}