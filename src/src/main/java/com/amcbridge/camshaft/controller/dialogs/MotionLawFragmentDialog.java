package com.amcbridge.camshaft.controller.dialogs;

import com.amcbridge.camshaft.controller.controls.GeometryTextField;
import com.amcbridge.camshaft.model.MotionLawFragment;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.math.BigDecimal;

import static com.amcbridge.camshaft.utils.Constants.*;

public class MotionLawFragmentDialog extends Dialog<MotionLawFragment> {

    public MotionLawFragmentDialog() {
        setHeaderText(null);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        TextField functionInputField = new TextField();
        GeometryTextField stepInputField = new GeometryTextField(POSITIVE_DOUBLE_PATTERN);
        GeometryTextField startInputField = new GeometryTextField(POSITIVE_DOUBLE_PATTERN);
        GeometryTextField endInputField = new GeometryTextField(POSITIVE_DOUBLE_PATTERN);
        gridPane.addRow(0, new Label("Y(x)="), functionInputField, new Label("Step:"), stepInputField);
        gridPane.addRow(1, new Label("Start:"), startInputField, new Label("End:"), endInputField);
        Node okButton = getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(Bindings.isEmpty(functionInputField.textProperty())
                .or(Bindings.isEmpty(stepInputField.textProperty()))
                .or(Bindings.isEmpty(startInputField.textProperty()))
                .or(Bindings.isEmpty(endInputField.textProperty())));
        getDialogPane().setContent(gridPane);
        setResultConverter(dialogButton -> (dialogButton != ButtonType.OK) ? null
                : new MotionLawFragment(functionInputField.getText(),
                        new BigDecimal(stepInputField.getText()),
                        new BigDecimal(startInputField.getText()),
                        new BigDecimal(endInputField.getText())));
    }

}