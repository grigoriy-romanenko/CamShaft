package com.amcbridge.camshaft.controller.controls;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class GeometryTextField extends TextField {

    private String parameter;
    private String pattern;

    public GeometryTextField() {
        super();
        setTextFormatter(new TextFormatter<>(
                change -> change.getControlNewText().matches(getPattern()) ? change : null));
    }

    public GeometryTextField(String pattern) {
        super();
        setPattern(pattern);
        setTextFormatter(new TextFormatter<>(
                change -> change.getControlNewText().matches(getPattern()) ? change : null));
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

}