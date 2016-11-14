package com.amcbridge.camshaft;

import com.amcbridge.camshaft.controller.Context;
import com.amcbridge.camshaft.controller.GeometryController;
import com.amcbridge.camshaft.controller.MotionLawController;
import com.amcbridge.camshaft.controller.ProfileController;
import com.amcbridge.camshaft.controller.dialogs.MovePointDialog;
import com.amcbridge.camshaft.model.CamShaft;
import com.amcbridge.camshaft.model.FollowerType;
import com.amcbridge.camshaft.model.validators.CamShaftValidator;
import com.amcbridge.camshaft.model.validators.MotionLawFragmentValidator;
import com.amcbridge.camshaft.service.motionlaw.MotionLawServiceImpl;
import com.amcbridge.camshaft.service.profile.ProfileServiceImpl;
import com.amcbridge.camshaft.service.profile.central.CentralProfileCalculatorsContainer;
import com.amcbridge.camshaft.service.profile.central.RotatingCentralProfileCalculator;
import com.amcbridge.camshaft.service.profile.central.TranslatingCentralProfileCalculator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static final String GEOMETRY_FXML_FILE = "fxml/geometry.fxml";
    private static final String MOTION_LAW_FXML_FILE = "fxml/motionlaw.fxml";
    private static final String PROFILE_FXML_FILE = "fxml/profile.fxml";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setResizable(false);
        Context context = new Context();
        CamShaft camShaft = new CamShaft();
        context.setCamShaft(camShaft);
        context.setMotionLawService(new MotionLawServiceImpl(new MotionLawFragmentValidator()));
        CentralProfileCalculatorsContainer calculatorsContainer = new CentralProfileCalculatorsContainer();
        calculatorsContainer.addCalculator(FollowerType.TRANSLATING, new TranslatingCentralProfileCalculator());
        calculatorsContainer.addCalculator(FollowerType.ROTATING, new RotatingCentralProfileCalculator());
        context.setProfileService(new ProfileServiceImpl(new CamShaftValidator(), calculatorsContainer));
        context.setStage(primaryStage);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource(GEOMETRY_FXML_FILE));
        loader.setController(new GeometryController(context));
        context.setGeometryScene(new Scene(loader.load()));

        loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource(MOTION_LAW_FXML_FILE));
        loader.setController(new MotionLawController(context, new MovePointDialog()));
        context.setMotionLawScene(new Scene(loader.load()));

        loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource(PROFILE_FXML_FILE));
        loader.setController(new ProfileController(context));
        context.setProfileScene(new Scene(loader.load()));

        primaryStage.setScene(context.getGeometryScene());
        primaryStage.show();
    }

}