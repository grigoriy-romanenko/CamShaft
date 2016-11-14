package com.amcbridge.camshaft.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static javafx.scene.chart.XYChart.Data;

public class CamShaft {

    private FollowerType followerType;
    private Map<String, BigDecimal> params;
    private ObservableList<Data<Number, Number>> motionLaw;
    private ObservableList<Data<Number, Number>> centralProfile;
    private ObservableList<Data<Number, Number>> practicalProfile;

    public CamShaft() {
        setParams(new HashMap<>());
        motionLaw = FXCollections.observableArrayList();
        centralProfile = FXCollections.observableArrayList();
        practicalProfile = FXCollections.observableArrayList();
    }

    public FollowerType getFollowerType() {
        return followerType;
    }

    public void setFollowerType(FollowerType followerType) {
        Objects.requireNonNull(followerType);
        this.followerType = followerType;
    }

    public Map<String, BigDecimal> getParams() {
        return params;
    }

    public void setParams(Map<String, BigDecimal> params) {
        Objects.requireNonNull(params);
        this.params = params;
    }

    public ObservableList<Data<Number, Number>> getCentralProfile() {
        return centralProfile;
    }

    public ObservableList<Data<Number, Number>> getPracticalProfile() {
        return practicalProfile;
    }

    public ObservableList<Data<Number, Number>> getMotionLaw() {
        return motionLaw;
    }

}