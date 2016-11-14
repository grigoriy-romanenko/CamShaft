package com.amcbridge.camshaft.controller.dialogs;

import com.amcbridge.camshaft.controller.controls.GeometryTextField;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.math.BigDecimal;

import static com.amcbridge.camshaft.utils.Constants.*;

public class MovePointDialog extends Dialog<Pair<BigDecimal, BigDecimal>> {

    private GeometryTextField xInputField;
    private GeometryTextField yInputField;

    public MovePointDialog() {
        setHeaderText(null);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        xInputField = new GeometryTextField(POSITIVE_DOUBLE_PATTERN);
        yInputField = new GeometryTextField(DOUBLE_PATTERN);
        gridPane.addRow(0, new Label("X"), xInputField);
        gridPane.addRow(1, new Label("Y"), yInputField);
        Node okButton = getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(Bindings.isEmpty(xInputField.textProperty()).
                or(Bindings.isEmpty(yInputField.textProperty())));
        getDialogPane().setContent(gridPane);
        setResultConverter(dialogButton -> (dialogButton != ButtonType.OK) ? null
                : new Pair<>(new BigDecimal(xInputField.getText()), new BigDecimal(yInputField.getText())));
    }

    public void setText(String xInputFieldText, String yInputFieldText) {
        xInputField.setText(xInputFieldText);
        yInputField.setText(yInputFieldText);
    }

}