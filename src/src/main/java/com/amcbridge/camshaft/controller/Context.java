package com.amcbridge.camshaft.controller;

import com.amcbridge.camshaft.model.CamShaft;
import com.amcbridge.camshaft.service.motionlaw.MotionLawService;
import com.amcbridge.camshaft.service.profile.ProfileService;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Context {

    private Stage stage;
    private Scene geometryScene;
    private Scene motionLawScene;
    private Scene profileScene;
    private CamShaft camShaft;
    private MotionLawService motionLawService;
    private ProfileService profileService;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Scene getGeometryScene() {
        return geometryScene;
    }

    public void setGeometryScene(Scene geometryScene) {
        this.geometryScene = geometryScene;
    }

    public Scene getMotionLawScene() {
        return motionLawScene;
    }

    public void setMotionLawScene(Scene motionLawScene) {
        this.motionLawScene = motionLawScene;
    }

    public Scene getProfileScene() {
        return profileScene;
    }

    public void setProfileScene(Scene profileScene) {
        this.profileScene = profileScene;
    }

    public MotionLawService getMotionLawService() {
        return motionLawService;
    }

    public void setMotionLawService(MotionLawService motionLawService) {
        this.motionLawService = motionLawService;
    }

    public ProfileService getProfileService() {
        return profileService;
    }

    public void setProfileService(ProfileService profileService) {
        this.profileService = profileService;
    }

    public CamShaft getCamShaft() {
        return camShaft;
    }

    public void setCamShaft(CamShaft camShaft) {
        this.camShaft = camShaft;
    }

}