package com.example.mobilepayprojekt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
    private void opretBruger() {
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
            statusLabel.setText("Ny bruger oprettet: " + dbSql.formaterNavn(fnavn) + " " + dbSql.formaterNavn(enavn));
        } catch (Exception e) {
            statusLabel.setText("Fejl: " + e.getMessage());
        }
    }
    @FXML
    private void onTilbage(ActionEvent event) {
        NavigationUtil.goToHomeScreen((Node) event.getSource());
    }
}
