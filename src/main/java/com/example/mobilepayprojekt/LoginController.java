package com.example.mobilepayprojekt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField mobilNrField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final DbSql dbSql = new DbSql();

    public void initialize() {
        mobilNrField.textProperty().addListener((observable, oldValue, newValue) -> clearErrorMessage());
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> clearErrorMessage());
    }

    private void clearErrorMessage() {
        errorLabel.setText("");
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String mobilNr = mobilNrField.getText();
        String adgangskode = passwordField.getText();

        if (dbSql.validateLogin(mobilNr, adgangskode)) {
            Bruger loggedInUser = dbSql.getBruger(mobilNr);
            Session.setCurrentUser(loggedInUser);
            openMainScreen(event);
        } else {
            errorLabel.setText("Forkert mobilnummer eller adgangskode.");
        }
    }

    private void openMainScreen(ActionEvent event) {
        NavigationUtil.goToMainScreen((Node) event.getSource());
    }

    @FXML
    private void onTilbage(ActionEvent event) {
        NavigationUtil.goToHomeScreen((Node) event.getSource());
    }
}
