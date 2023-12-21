package com.example.mobilepayprojekt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class NewKontiController {

    @FXML
    private TextField kontoNrField;

    @FXML
    private Label kontoLabel;

    private final DbSql dbSql = new DbSql();

    public void initialize() {
        kontoNrField.textProperty().addListener((observable, oldValue, newValue) -> clearErrorMessage());
    }

    private void clearErrorMessage() {
        kontoLabel.setText("");
    }

    @FXML
    private void handleCreateAccount() {
        String kontoNr = kontoNrField.getText();
        try {
            Bruger currentUser = Session.getCurrentUser();
            dbSql.opretKonto(kontoNr, currentUser);
            Session.setCurrentUser(currentUser);

            kontoLabel.setText("Konto oprettet.");
        } catch (Exception e) {
            kontoLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void onTilbageFraNewKonto(ActionEvent event) {
        NavigationUtil.goToKontiScreen((Node) event.getSource());
    }
}
