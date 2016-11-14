package com.amcbridge.camshaft.controller;

import com.amcbridge.camshaft.controller.controls.GeometryTextField;
import com.amcbridge.camshaft.model.FollowerType;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class GeometryController {

    private static final String PARAMS_WERE_NOT_ENTERED_MSG = "Please fill out all required fields";

    @FXML
    private ChoiceBox<FollowerType> followerTypeChoiceBox;
    @FXML
    private Pane translatingParamsPane;
    @FXML
    private Pane rotatingParamsPane;
    @FXML
    private Button nextSceneButton;

    private Context context;
    private Map<FollowerType, Pane> panes;

    public GeometryController(Context context) {
        this.context = context;
    }

    /**
     * invokes on FXMLLoader.load()
     */
    @FXML
    private void initialize() {
        initPanes();
        initFollowerTypeChoiceBox();
        initNextSceneButton();
    }

    private void initPanes() {
        panes = new HashMap<>();
        panes.put(FollowerType.TRANSLATING, translatingParamsPane);
        panes.put(FollowerType.ROTATING, rotatingParamsPane);
    }

    private void initFollowerTypeChoiceBox() {
        followerTypeChoiceBox.setItems(FXCollections.observableList(Arrays.asList(FollowerType.values())));
        followerTypeChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener(new FollowerTypeChoiceBoxChangeListener());
        followerTypeChoiceBox.getSelectionModel().selectFirst();
    }

    private void initNextSceneButton() {
        nextSceneButton.setOnAction(event -> {
            FollowerType followerType = followerTypeChoiceBox.getSelectionModel().getSelectedItem();
            Set<GeometryTextField> textFields = panes.get(followerType).lookupAll(".text-field").stream()
                    .map(node -> (GeometryTextField) node).collect(Collectors.toSet());
            Map<String, BigDecimal> userInput = readParams(textFields);
            if (userInput == null) return;
            context.getCamShaft().setParams(userInput);
            context.getCamShaft().setFollowerType(followerType);
            context.getStage().setScene(context.getMotionLawScene());
        });
    }

    private Map<String, BigDecimal> readParams(Set<GeometryTextField> textFields) {
        Map<String, BigDecimal> params = new HashMap<>();
        for (GeometryTextField textField : textFields) {
            if (textField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, PARAMS_WERE_NOT_ENTERED_MSG);
                alert.setHeaderText(null);
                alert.showAndWait();
                return null;
            }
            params.put(textField.getParameter(), new BigDecimal(textField.getText()));
        }
        return params;
    }

    private class FollowerTypeChoiceBoxChangeListener implements ChangeListener<FollowerType> {

        @Override
        public void changed(ObservableValue<? extends FollowerType> observable, FollowerType oldValue, FollowerType newValue) {
            panes.values().forEach(pane -> pane.setVisible(false));
            panes.get(newValue).setVisible(true);
        }

    }

}