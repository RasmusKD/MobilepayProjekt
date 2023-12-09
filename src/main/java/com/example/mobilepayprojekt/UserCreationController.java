package com.example.mobilepayprojekt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class UserCreationController {

    @FXML
    private TextField fnavnField;
    @FXML
    private TextField enavnField;
    @FXML
    private TextField mobilNrField;
    @FXML
    private PasswordField adgangskodeField;

    @FXML
    private PasswordField bekraeftAdgangskodeField;
    @FXML
    private Label statusLabel;

    private final DbSql dbSql = new DbSql();

    @FXML
    protected void opretBruger() {
        String fnavn = fnavnField.getText();
        String enavn = enavnField.getText();
        String mobilNr = mobilNrField.getText();
        String adgangskode = adgangskodeField.getText();
        String bekraeftAdgangskode = bekraeftAdgangskodeField.getText();

        if (!adgangskode.equals(bekraeftAdgangskode)) {
            statusLabel.setText("Adgangskoderne matcher ikke.");
            return;
        }

        try {
            Bruger nyBruger = new Bruger(0, fnavn, enavn, mobilNr, adgangskode);
            dbSql.opretBruger(nyBruger);
            statusLabel.setText("Ny bruger oprettet: " + fnavn + " " + enavn);
        } catch (Exception e) {
            statusLabel.setText("Fejl: " + e.getMessage());
        }
    }
    @FXML
    protected void onTilbage(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/homeScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 360, 640);
            scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
