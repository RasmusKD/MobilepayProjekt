package com.example.mobilepayprojekt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    @FXML
    private void onLogin(ActionEvent event) {
        NavigationUtil.goToLoginScreen((Node) event.getSource());
    }

    @FXML
    private void onOpretBruger(ActionEvent event) {
        NavigationUtil.goToUserCreationScreen((Node) event.getSource());
    }
}
