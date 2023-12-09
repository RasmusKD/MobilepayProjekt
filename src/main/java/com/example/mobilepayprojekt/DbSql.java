package com.example.mobilepayprojekt;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class DbSql {
    private Connection connection;
    private final Statement stmt;
    private Statement stmt1;

    DbSql() {
        connection = null;
        stmt = null;
        try {
            String url = "jdbc:sqlite:C://Users/Rasmus/Desktop/Mobilepay.db";
            connection = DriverManager.getConnection(url);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Bruger getBruger(String mobilNr) {
        Bruger b = null;
        try {
            String sql = "SELECT * FROM Bruger WHERE mobilnr = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, mobilNr);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int brugerId = rs.getInt("bruger_id");
                String fnavn = rs.getString("fnavn");
                String enavn = rs.getString("enavn");
                String retrievedMobilNr = rs.getString("mobilnr");
                String adgangskode = rs.getString("adgangskode");
                b = new Bruger(brugerId, fnavn, enavn, retrievedMobilNr, adgangskode);
            }
            rs.close();
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return b;
    }


    public void opretBruger(Bruger bruger) throws Exception {
        // Tjekker at alle felter er udfyldte
        if (bruger.getFnavn() == null || bruger.getFnavn().isEmpty() ||
            bruger.getEnavn() == null || bruger.getEnavn().isEmpty() ||
            bruger.getMobilNr() == null || bruger.getMobilNr().isEmpty() ||
            bruger.getAdgangskode() == null || bruger.getAdgangskode().isEmpty()) {
            throw new Exception("Alle felter skal være udfyldt.");
        }
        // namePattern er regex for en Unicode klasse som indeholder alle bogstaver, dette er for at undgå tal og tegn i navne og havde adgang til danske bogstaver.
        String namePattern = "\\p{L}+";
        if (!bruger.getFnavn().matches(namePattern) || !bruger.getEnavn().matches(namePattern)) {
            throw new Exception("Navn og efternavn må kun indeholde bogstaver.");
        }

        // Tjekker, at mobilNr kun indeholder tal og er 8 cifre langt
        if (!bruger.getMobilNr().matches("\\d{8}")) {
            throw new Exception("Telefonnummer må kun indeholde tal og være 8 cifre langt.");
        }

        // Tjekker, om en en bruger eksisterer med samme mobilnr
        if (getBruger(bruger.getMobilNr()) != null) {
            throw new Exception("En bruger med dette mobilnummer eksisterer allerede.");
        }

        // Hasher adgangskoden
        String hashedAdgangskode = BCrypt.hashpw(bruger.getAdgangskode(), BCrypt.gensalt());
        bruger.setAdgangskode(hashedAdgangskode);

        try {
            String sql = "INSERT INTO Bruger (fnavn, enavn, mobilNr, adgangskode) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, bruger.getFnavn());
            stmt.setString(2, bruger.getEnavn());
            stmt.setString(3, bruger.getMobilNr());
            stmt.setString(4, hashedAdgangskode);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }

    public boolean validateLogin(String mobilNr, String adgangskode) {
        Bruger bruger = getBruger(mobilNr);
        return bruger != null && BCrypt.checkpw(adgangskode, bruger.getAdgangskode());
    }

}


