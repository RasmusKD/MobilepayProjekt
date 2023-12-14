package com.example.mobilepayprojekt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

import java.sql.SQLException;
import java.util.List;

public class KontiController {

    public VBox andreKonti;
    private DbSql dbSql = new DbSql();

    @FXML
    private Label primaryKontoLabel;

    public void initialize() {
        primaryKonto();
        konti();
    }
    private void primaryKonto() {
        Bruger currentUser = Session.getCurrentUser();
        Konto primaryKonto = currentUser.getPrimaryKonto();

        if (primaryKonto != null) {
            String kontoNr = primaryKonto.getKontoNr();
            kontoNr = kontoNr.substring(0, 4) + " " + kontoNr.substring(4);
            primaryKontoLabel.setText(kontoNr);
        } else {
            primaryKontoLabel.setText("Du har ikke en primær konto");
        }
    }

    private void konti() {
        Bruger currentUser = Session.getCurrentUser();
        List<Konto> allKonti = currentUser.getKonti();

        //rydder alle konti til når vi refresher
        andreKonti.getChildren().clear();

        for (Konto konto : allKonti) {
            if (!konto.isPrimary()) {
                HBox kontoInfoBox = new HBox(15);
                kontoInfoBox.setAlignment(Pos.CENTER_LEFT);
                kontoInfoBox.getStyleClass().add("konto-box");

                SVGPath svgIcon = new SVGPath();
                svgIcon.setStyle("-fx-fill: #3a3244");
                svgIcon.setContent("M10.8321 1.24802C11.5779 0.917327 12.4221 0.917327 13.1679 1.24802L21.7995 5.0754C23.7751 5.95141 23.1703 9 21.0209 9H2.97906C0.829669 9 0.224891 5.9514 2.20047 5.0754L10.8321 1.24802ZM12.3893 3.12765C12.1407 3.01742 11.8593 3.01742 11.6107 3.12765L3.41076 6.76352C3.31198 6.80732 3.34324 6.95494 3.45129 6.95494H20.5487C20.6568 6.95494 20.688 6.80732 20.5892 6.76352L12.3893 3.12765Z M2 22C2 21.4477 2.44772 21 3 21H21C21.5523 21 22 21.4477 22 22C22 22.5523 21.5523 23 21 23H3C2.44772 23 2 22.5523 2 22Z M11 19C11 19.5523 11.4477 20 12 20C12.5523 20 13 19.5523 13 19V11C13 10.4477 12.5523 10 12 10C11.4477 10 11 10.4477 11 11V19Z M6 20C5.44772 20 5 19.5523 5 19L5 11C5 10.4477 5.44771 10 6 10C6.55228 10 7 10.4477 7 11L7 19C7 19.5523 6.55229 20 6 20Z M17 19C17 19.5523 17.4477 20 18 20C18.5523 20 19 19.5523 19 19V11C19 10.4477 18.5523 10 18 10C17.4477 10 17 10.4477 17 11V19Z");

                String kontoNr = konto.getKontoNr();
                kontoNr = kontoNr.substring(0, 4) + " " + kontoNr.substring(4);

                Label kontoLabel = new Label(kontoNr);
                kontoLabel.setStyle("-fx-font-size: 16px;");

                Region region = new Region();
                HBox.setHgrow(region, Priority.ALWAYS);

                SVGPath svgIcon2 = new SVGPath();
                svgIcon2.setStyle("-fx-fill: #5976fd");
                svgIcon2.setContent("M8.96173 18.9109L9.42605 18.3219L8.96173 18.9109ZM12 5.50063L11.4596 6.02073C11.601 6.16763 11.7961 6.25063 12 6.25063C12.2039 6.25063 12.399 6.16763 12.5404 6.02073L12 5.50063ZM15.0383 18.9109L15.5026 19.4999L15.0383 18.9109ZM9.42605 18.3219C7.91039 17.1271 6.25307 15.9603 4.93829 14.4798C3.64922 13.0282 2.75 11.3345 2.75 9.1371H1.25C1.25 11.8026 2.3605 13.8361 3.81672 15.4758C5.24723 17.0866 7.07077 18.3752 8.49742 19.4999L9.42605 18.3219ZM2.75 9.1371C2.75 6.98623 3.96537 5.18252 5.62436 4.42419C7.23607 3.68748 9.40166 3.88258 11.4596 6.02073L12.5404 4.98053C10.0985 2.44352 7.26409 2.02539 5.00076 3.05996C2.78471 4.07292 1.25 6.42503 1.25 9.1371H2.75ZM8.49742 19.4999C9.00965 19.9037 9.55954 20.3343 10.1168 20.6599C10.6739 20.9854 11.3096 21.25 12 21.25V19.75C11.6904 19.75 11.3261 19.6293 10.8736 19.3648C10.4213 19.1005 9.95208 18.7366 9.42605 18.3219L8.49742 19.4999ZM15.5026 19.4999C16.9292 18.3752 18.7528 17.0866 20.1833 15.4758C21.6395 13.8361 22.75 11.8026 22.75 9.1371H21.25C21.25 11.3345 20.3508 13.0282 19.0617 14.4798C17.7469 15.9603 16.0896 17.1271 14.574 18.3219L15.5026 19.4999ZM22.75 9.1371C22.75 6.42503 21.2153 4.07292 18.9992 3.05996C16.7359 2.02539 13.9015 2.44352 11.4596 4.98053L12.5404 6.02073C14.5983 3.88258 16.7639 3.68748 18.3756 4.42419C20.0346 5.18252 21.25 6.98623 21.25 9.1371H22.75ZM14.574 18.3219C14.0479 18.7366 13.5787 19.1005 13.1264 19.3648C12.6739 19.6293 12.3096 19.75 12 19.75V21.25C12.6904 21.25 13.3261 20.9854 13.8832 20.6599C14.4405 20.3343 14.9903 19.9037 15.5026 19.4999L14.574 18.3219Z");

                Button setPrimaryButton = new Button();
                setPrimaryButton.getStyleClass().add("settings-button");
                setPrimaryButton.setGraphic(svgIcon2);
                setPrimaryButton.setOnAction(event -> {
                    setPrimaryKonto(konto);
                });

                SVGPath svgIcon3 = new SVGPath();
                svgIcon3.setStyle("-fx-fill: #e85248");
                svgIcon3.setContent("M6 4.5858L10.2929 0.29289C10.6834 -0.09763 11.3166 -0.09763 11.7071 0.29289C12.0976 0.68342 12.0976 1.31658 11.7071 1.70711L7.4142 6L11.7071 10.2929C12.0976 10.6834 12.0976 11.3166 11.7071 11.7071C11.3166 12.0976 10.6834 12.0976 10.2929 11.7071L6 7.4142L1.70711 11.7071C1.31658 12.0976 0.68342 12.0976 0.29289 11.7071C-0.09763 11.3166 -0.09763 10.6834 0.29289 10.2929L4.5858 6L0.29289 1.70711C-0.09763 1.31658 -0.09763 0.68342 0.29289 0.29289C0.68342 -0.09763 1.31658 -0.09763 1.70711 0.29289L6 4.5858z");

                Button sletKontoButton = new Button();
                sletKontoButton.getStyleClass().add("settings-button");
                sletKontoButton.setGraphic(svgIcon3);
                sletKontoButton.setOnAction(event -> {
                    deleteKonto(konto);
                });

                kontoInfoBox.getChildren().addAll(svgIcon, kontoLabel, sletKontoButton, svgIcon3, region, setPrimaryButton, svgIcon2);

                andreKonti.getChildren().add(kontoInfoBox);
            }
        }
    }

    private void deleteKonto(Konto konto) {
        try {
            dbSql.deleteKonto(konto.getKontoId());
            Bruger currentUser = Session.getCurrentUser();
            currentUser.fjernKonto(konto.getKontoId());
            konti();
        } catch (SQLException e) {
        }
    }

    private void setPrimaryKonto(Konto konto) {
        Bruger currentUser = Session.getCurrentUser();
        List<Konto> alleKonti = currentUser.getKonti();

        try {
            // Loop alle eksisterende konti og sæt dem til ikke at være primære
            for (Konto k : alleKonti) {
                k.setPrimary(false);
                dbSql.updatePrimaryKonto(k.getKontoId(), false);
            }
            // Sæt den valgte konto som primær
            konto.setPrimary(true);
            dbSql.updatePrimaryKonto(konto.getKontoId(), true);

            // Opdater UI med ny primær konto
            primaryKonto();
            konti();

        } catch (SQLException e) {
        }
    }

    @FXML
    private void onTilbageFraKonto(ActionEvent event) {
        NavigationUtil.goToMainScreenTab((Node) event.getSource(), "userSettingsView");
    }
    @FXML
    private void onNewKonto(ActionEvent event) {
        NavigationUtil.goToNewKontiScreen((Node) event.getSource());
    }
}
