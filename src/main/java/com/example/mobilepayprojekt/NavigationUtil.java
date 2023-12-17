package com.example.mobilepayprojekt;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NavigationUtil {

    private static Object goToScreen(Node source, String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(NavigationUtil.class.getResource(fxmlFile));
            Scene scene = new Scene(fxmlLoader.load(), 360, 640);
            scene.getStylesheets().add(NavigationUtil.class.getResource("/styles/style.css").toExternalForm());
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(scene);

            return fxmlLoader.getController();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void goToHomeScreen(Node source) {
        goToScreen(source, "/fxml/homeScreen.fxml");
    }
    public static void goToUserCreationScreen(Node source) {
        goToScreen(source, "/fxml/userCreationScreen.fxml");
    }
    public static void goToLoginScreen(Node source) {
        goToScreen(source, "/fxml/loginScreen.fxml");
    }
    public static void goToMainScreen(Node source) {
        goToScreen(source, "/fxml/mainScreen.fxml");
    }
    public static void goToKontiScreen(Node source) {
        goToScreen(source, "/fxml/kontiScreen.fxml");
    }
    public static void goToNewKontiScreen(Node source) {
        goToScreen(source, "/fxml/newKontiScreen.fxml");
    }

    /*
     Skifter til mainscreen og aktiverer en specifik fane.
     Efter skærm skiftet, henter den MainScreenController ved hjælp af typecast så vi kan tilgå metoden setActiveTab.
     */
    public static void goToMainScreenTab(Node source, String tabName) {
        MainScreenController mainScreenController = (MainScreenController) goToScreen(source, "/fxml/mainScreen.fxml");
        mainScreenController.setActiveTab(tabName);
    }
}
