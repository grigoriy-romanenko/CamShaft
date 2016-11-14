package com.amcbridge.camshaft.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;

public class ProfileController {

    @FXML
    private LineChart<Number, Number> chart;
    @FXML
    private Button prevSceneButton;

    private Context context;

    public ProfileController(Context context) {
        this.context = context;
    }

    /**
     * invokes on FXMLLoader.load()
     */
    @FXML
    private void initialize() {
        chart.getData().add(new XYChart.Series<>(context.getCamShaft().getCentralProfile()));
        chart.getData().add(new XYChart.Series<>(context.getCamShaft().getPracticalProfile()));
        prevSceneButton.setOnAction(event -> context.getStage().setScene(context.getMotionLawScene()));
    }

}
