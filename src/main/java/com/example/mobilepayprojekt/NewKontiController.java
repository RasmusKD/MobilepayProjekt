package com.example.mobilepayprojekt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class NewKontiController {
    private MainScreenController mainScreenController;

    public void setMainScreenController(MainScreenController controller) {
        this.mainScreenController = controller;
    }
    @FXML
    private TextField kontoNrField;

    @FXML
    private Label errorLabel;

    private DbSql dbSql = new DbSql();

    @FXML
    protected void handleCreateAccount() {
        String kontoNr = kontoNrField.getText();
        try {
            Bruger currentUser = Session.getCurrentUser();
            dbSql.opretKonto(kontoNr, currentUser);
            Session.setCurrentUser(currentUser);
        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void onTilbageFraNewKonto(ActionEvent event) {
        NavigationUtil.goToKontiScreen((Node) event.getSource());
    }
}
