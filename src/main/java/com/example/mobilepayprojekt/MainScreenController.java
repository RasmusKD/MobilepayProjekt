package com.example.mobilepayprojekt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

public class MainScreenController {

    public Label userNameLabel;
    public Label mobilNrLabel;
    public SVGPath menuIcon;
    public SVGPath transactionIcon;
    public SVGPath homeIcon;
    public Label homeLabel;
    public Label transactionLabel;
    public Label menuLabel;
    public Label initialsLabel;

    private DbSql dbSql = new DbSql();

    @FXML
    private VBox mainView;

    @FXML
    private VBox transactionsView;

    @FXML
    private VBox userSettingsView;

    @FXML
    private TextField beloebField;

    @FXML
    private TextField modtagerMobilNrField;

    @FXML
    private ComboBox<Konto> kontoValgComboBox;

    @FXML
    private Label mainLabel;

    public void initialize() {
        setActiveView(mainView);
        opdaterKontoValgComboBox();
        Bruger currentUser = Session.getCurrentUser();
        if (currentUser != null) {
            String initials = getInitials(currentUser.getFnavn(), currentUser.getEnavn());
            initialsLabel.setText(initials);

            userNameLabel.setText(currentUser.getFnavn() + " " + currentUser.getEnavn());
            mobilNrLabel.setText("+45 " + currentUser.getMobilNr());
        }
    }

    private void opdaterKontoValgComboBox() {
        Bruger currentUser = Session.getCurrentUser();
        if (currentUser != null) {
            ObservableList<Konto> konti = FXCollections.observableArrayList(currentUser.getKonti());
            kontoValgComboBox.setItems(konti);

            // Sæt den primære konto som standardkonto
            kontoValgComboBox.getSelectionModel().select(currentUser.getPrimaryKonto());

            // Tilpas visningen af konti i dropdown til kontonr
            kontoValgComboBox.setCellFactory(lv -> new ListCell<Konto>() {
                @Override
                protected void updateItem(Konto konto, boolean empty) {
                    super.updateItem(konto, empty);
                    setText(empty ? null : konto.getKontoNr());
                }
            });

            kontoValgComboBox.setButtonCell(new ListCell<Konto>() {
                @Override
                protected void updateItem(Konto konto, boolean empty) {
                    super.updateItem(konto, empty);
                    setText(empty ? null : konto.getKontoNr());
                }
            });
        }
    }

    @FXML
    protected void sendPenge() {
        try {
            double beloeb = Double.parseDouble(beloebField.getText());
            String modtagerMobilNr = modtagerMobilNrField.getText();
            Bruger currentUser = Session.getCurrentUser();
            Konto afsenderKonto = kontoValgComboBox.getValue();

            if (afsenderKonto == null) {
                throw new Exception("Vælg venligst en konto.");
            }

            if (beloeb <= 0) {
                throw new Exception("Beløbet skal være større end 0.");
            }

            Bruger modtager = dbSql.getBruger(modtagerMobilNr);
            if (modtager == null) {
                throw new Exception("Modtageren findes ikke.");
            }

            dbSql.overfoerPenge(afsenderKonto.getKontoNr(), modtagerMobilNr, beloeb);
            dbSql.tilfoejTransaktion(currentUser.getBrugerId(), modtager.getBrugerId(), beloeb);

            mainLabel.setText("Overførsel udført succesfuldt.");

        } catch (NumberFormatException e) {
            mainLabel.setText("Beløbet skal være et gyldigt tal.");
        } catch (Exception e) {
            mainLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void showMainView() {
        setActiveView(mainView);
    }

    @FXML
    private void showTransactionsView() {
        setActiveView(transactionsView);
    }

    @FXML
    private void showUserSettingsView() {
        setActiveView(userSettingsView);
    }

    private void setActiveView(VBox activeView) {
        mainView.setVisible(activeView == mainView);
        transactionsView.setVisible(activeView == transactionsView);
        userSettingsView.setVisible(activeView == userSettingsView);

        updateIconAndLabelStyle(homeIcon, homeLabel, activeView == mainView);
        updateIconAndLabelStyle(transactionIcon, transactionLabel, activeView == transactionsView);
        updateIconAndLabelStyle(menuIcon, menuLabel, activeView == userSettingsView);
    }

    //Til at sætte aktiv tab når man går tilbage til main-view
    public void setActiveTab(String tabName) {
        if ("mainView".equals(tabName)) {
            setActiveView(mainView);
        } else if ("transactionsView".equals(tabName)) {
            setActiveView(transactionsView);
        } else if ("userSettingsView".equals(tabName)) {
            setActiveView(userSettingsView);
        }
    }

    private void updateIconAndLabelStyle(SVGPath icon, Label label, boolean isActive) {
        icon.getStyleClass().clear();
        label.getStyleClass().clear();

        if (isActive) {
            icon.getStyleClass().add("icon-active");
            label.getStyleClass().add("icon-label-active");
        } else {
            icon.getStyleClass().add("icon-inactive");
            label.getStyleClass().add("icon-label-inactive");
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        Session.clear();

        NavigationUtil.goToHomeScreen((Node) event.getSource());
    }
    @FXML
    private void onKonto(ActionEvent event) {
        NavigationUtil.goToKontiScreen((Node) event.getSource());
    }

    @FXML
    private String getInitials(String firstName, String lastName) {
        return String.valueOf(firstName.charAt(0)) + lastName.charAt(0);
    }
}
