module com.example.mobilepayprojekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;


    opens com.example.mobilepayprojekt to javafx.fxml;
    exports com.example.mobilepayprojekt;
}