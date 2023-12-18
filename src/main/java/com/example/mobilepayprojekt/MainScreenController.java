package com.example.mobilepayprojekt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;

import java.util.List;

public class MainScreenController {

    @FXML
    private Label userNameLabel;
    @FXML
    private Label mobilNrLabel;
    @FXML
    private SVGPath menuIcon;
    @FXML
    private SVGPath transactionIcon;
    @FXML
    private SVGPath homeIcon;
    @FXML
    private Label homeLabel;
    @FXML
    private Label transactionLabel;
    @FXML
    private Label menuLabel;
    @FXML
    private Label initialsLabel;
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
    @FXML
    private VBox transaktioner;

    private final DbSql dbSql = new DbSql();

    public void initialize() {
        setActiveView(mainView);
        opdaterKontoValgComboBox();
        Bruger currentUser = Session.getCurrentUser();
        transaktioner();
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
    private void sendPenge() {
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
            transaktioner();
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
    //public - vi typecaster til den fra i en anden klasse
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

    private void transaktioner() {
        Bruger currentUser = Session.getCurrentUser();
        List<Transaktion> allTransactions = currentUser.getTransaktioner();

        //rydder alle transaktioner til, når vi refresher
        transaktioner.getChildren().clear();

        for (Transaktion transaktion : allTransactions) {
            HBox transaktionInfoBox = new HBox(15);
            transaktionInfoBox.setAlignment(Pos.CENTER_LEFT);
            transaktionInfoBox.getStyleClass().add("konto-box");

            //Bestem om transaktionen er sendt eller modtaget
            boolean isSender = transaktion.getAfsenderId() == currentUser.getBrugerId();
            String transaktionType = isSender ? "Du overførte penge" : "Du modtog penge";

            //Hent navnet på den anden bruger i transaktionen
            Bruger andenBruger = dbSql.getBrugerById(isSender ? transaktion.getModtagerId() : transaktion.getAfsenderId());
            String andenBrugerNavn = andenBruger != null ? andenBruger.getFnavn() + " " + andenBruger.getEnavn() : "Ukendt";
            String initialer = andenBruger != null ? getInitials(andenBruger.getFnavn(), andenBruger.getEnavn()) : "??";

            //cirkel med initialer
            Circle initialerCircle = new Circle(18, Color.web("#7a7f85"));
            Label initialerLabel = new Label(initialer);
            initialerLabel.getStyleClass().add("initials-label");

            //Tilføj cirkel og initialer til stack
            StackPane initialerStack = new StackPane();
            initialerStack.getChildren().addAll(initialerCircle, initialerLabel);

            Label andenBrugerLabel = new Label(andenBrugerNavn);
            andenBrugerLabel.getStyleClass().add("bold-label");

            Label transaktionTypeLabel = new Label(transaktionType);
            transaktionTypeLabel.getStyleClass().add(isSender ? "sendt-label" : "modtaget-label");

            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);

            double beloeb = transaktion.getBeloeb();
            String beloebTekst = String.format("%s%.2f", isSender ? "-" : "", beloeb);
            Label beloebLabel = new Label(beloebTekst);
            beloebLabel.setTextFill(isSender ? Color.web("#c23c30") : Color.web("#4fb13f"));
            beloebLabel.getStyleClass().add("bold-label");

            VBox navnOgTypeBox = new VBox(andenBrugerLabel, transaktionTypeLabel);
            navnOgTypeBox.setAlignment(Pos.CENTER_LEFT);

            transaktionInfoBox.getChildren().addAll(initialerStack, navnOgTypeBox, region, beloebLabel);

            //Omvendt rækkefølge ved at sætte transaktionerne ind på den 0. plads
            transaktioner.getChildren().add(0, transaktionInfoBox);
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
