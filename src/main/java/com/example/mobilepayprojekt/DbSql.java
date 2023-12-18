package com.example.mobilepayprojekt;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class DbSql {
    private Connection connection;

    DbSql() {
        connection = null;
        Statement stmt = null;
        try {
            String url = "jdbc:sqlite:C://Users/Rasmus/Desktop/Mobilepay.db";
            connection = DriverManager.getConnection(url);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Bruger getBruger(String mobilNr) {
        Bruger bruger = null;
        try {
            String sqlBruger = "SELECT * FROM Bruger WHERE mobilnr = ?";
            PreparedStatement stmtBruger = connection.prepareStatement(sqlBruger);
            stmtBruger.setString(1, mobilNr);
            ResultSet rsBruger = stmtBruger.executeQuery();

            if (rsBruger.next()) {
                int brugerId = rsBruger.getInt("bruger_id");
                String fnavn = rsBruger.getString("fnavn");
                String enavn = rsBruger.getString("enavn");
                String dbMobilNr = rsBruger.getString("mobilnr");
                String adgangskode = rsBruger.getString("adgangskode");
                bruger = new Bruger(brugerId, fnavn, enavn, dbMobilNr, adgangskode);

                // Henter og tilføjer brugerens konti
                String sqlKonto = "SELECT * FROM Konto WHERE bruger_id = ?";
                PreparedStatement stmtKonto = connection.prepareStatement(sqlKonto);
                stmtKonto.setInt(1, brugerId);
                ResultSet rsKonto = stmtKonto.executeQuery();

                while (rsKonto.next()) {
                    int kontoId = rsKonto.getInt("konto_id");
                    String kontoNr = rsKonto.getString("konto_nr");
                    double saldo = rsKonto.getDouble("saldo");
                    boolean isPrimary = rsKonto.getBoolean("is_primary");
                    Konto konto = new Konto(kontoId, brugerId, kontoNr, saldo, isPrimary);
                    bruger.tilfoejKonto(konto, isPrimary);
                }

                rsKonto.close();
                stmtKonto.close();
                // Henter og tilføjer brugerens transaktioner
                String sqlTransaktion = "SELECT * FROM Transaktion WHERE afsender_id = ? OR modtager_id = ?";
                PreparedStatement stmtTransaktion = connection.prepareStatement(sqlTransaktion);
                stmtTransaktion.setInt(1, brugerId);
                stmtTransaktion.setInt(2, brugerId);
                ResultSet rsTransaktion = stmtTransaktion.executeQuery();

                while (rsTransaktion.next()) {
                    int transaktionId = rsTransaktion.getInt("transaktions_id");
                    int afsenderId = rsTransaktion.getInt("afsender_id");
                    int modtagerId = rsTransaktion.getInt("modtager_id");
                    int beloeb = rsTransaktion.getInt("beloeb");
                    String dato = rsTransaktion.getString("dato");
                    Transaktion transaktion = new Transaktion(transaktionId, afsenderId, modtagerId, beloeb, dato);
                    bruger.tilfoejTransaktion(transaktion);
                }

                rsTransaktion.close();
                stmtTransaktion.close();

            }

            rsBruger.close();
            stmtBruger.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return bruger;
    }

    public Bruger getBrugerById(int brugerId) {
        Bruger bruger = null;
        try {
            String sql = "SELECT * FROM Bruger WHERE bruger_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, brugerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String fnavn = rs.getString("fnavn");
                String enavn = rs.getString("enavn");
                String mobilNr = rs.getString("mobilnr");
                bruger = new Bruger(brugerId, fnavn, enavn, mobilNr);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bruger;
    }

    public void opretBruger(Bruger bruger) throws Exception {
        // Tjekker at alle felter er udfyldte
        if (bruger.getFnavn() == null || bruger.getFnavn().isEmpty() ||
            bruger.getEnavn() == null || bruger.getEnavn().isEmpty() ||
            bruger.getMobilNr() == null || bruger.getMobilNr().isEmpty() ||
            bruger.getAdgangskode() == null || bruger.getAdgangskode().isEmpty()) {
            throw new Exception("Alle felter skal være udfyldt.");
        }
        // namePattern er regex for en Unicode klasse som indeholder alle bogstaver,
        // dette er for at undgå tal og tegn i navne og havde adgang til danske bogstaver.
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

        // Vi kalder en metode som sætter forbogstav i navn og efternavn til stort
        // De følgende bogstaver bliver sat til at være små
        bruger.setFnavn(formaterNavn(bruger.getFnavn()));
        bruger.setEnavn(formaterNavn(bruger.getEnavn()));

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

    public String formaterNavn(String navn) {
        return navn.substring(0, 1).toUpperCase() + navn.substring(1).toLowerCase();
    }

    public Konto getKonto(int brugerId) {
        Konto k = null;
        try {
            String sql = "SELECT * FROM konto WHERE bruger_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, brugerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int kontoId = rs.getInt("konto_id");
                int dbBrugerId = rs.getInt("bruger_id");
                String kontoNr = rs.getString("konto_nr");
                double saldo = rs.getDouble("saldo");
                boolean isPrimary = rs.getBoolean("is_primary");
                k = new Konto(kontoId, dbBrugerId, kontoNr, saldo, isPrimary);
            }
            rs.close();
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return k;
    }

    public Konto getKontoExists(String kontoNr) {
        Konto k = null;
        try {
            String sql = "SELECT * FROM Konto WHERE konto_nr = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, kontoNr);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int kontoId = rs.getInt("konto_id");
                int brugerId = rs.getInt("bruger_id");
                double saldo = rs.getDouble("saldo");
                boolean isPrimary = rs.getBoolean("is_primary");
                k = new Konto(kontoId, brugerId, kontoNr, saldo, isPrimary);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return k;
    }

    public void opretKonto(String kontoNr, Bruger bruger) throws Exception {
        if (!kontoNr.matches("\\d{14}")) {
            throw new Exception("Konto og registrerings nr skal være 14 cifre.");
        }

        if (getKontoExists(kontoNr) != null) {
            throw new Exception("En konto med dette nummer eksisterer allerede.");
        }
        // Fast startbeløb, kan ændres i databasen under settings-tabellen.
        double startSaldo = Double.parseDouble(getSetting("startSaldo"));

        try {
            String sql = "INSERT INTO Konto (bruger_id, konto_nr, saldo, is_primary) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, bruger.getBrugerId());
            stmt.setString(2, kontoNr);
            stmt.setDouble(3, startSaldo);
            // Sæt som primær konto hvis det er brugerens første konto
            boolean setAsPrimary = bruger.getKonti().isEmpty();
            stmt.setBoolean(4, setAsPrimary);

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int kontoId = generatedKeys.getInt(1);
                Konto nyKonto = new Konto(kontoId, bruger.getBrugerId(), kontoNr, startSaldo, setAsPrimary);

                bruger.tilfoejKonto(nyKonto, setAsPrimary);
            }

            stmt.close();
        } catch (SQLException e) {
            throw new Exception("Fejl ved oprettelse af konto: " + e.getMessage());
        }
    }


    private boolean harBrugerKonto(int brugerId) {
        return getKonto(brugerId) != null;
    }

    public String getSetting(String key) {
        String value = null;
        try {
            String sql = "SELECT value FROM Settings WHERE key = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, key);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                value = rs.getString("value");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

    //validerer password (encrypted med bcrypt)
    public boolean validateLogin(String mobilNr, String adgangskode) {
        Bruger bruger = getBruger(mobilNr);
        return bruger != null && BCrypt.checkpw(adgangskode, bruger.getAdgangskode());
    }

    public void deleteKonto(int kontoId) throws SQLException {
        String sql = "DELETE FROM Konto WHERE konto_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, kontoId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public void overfoerPenge(String afsenderKontoNr, String modtagerMobilNr, double beloeb) throws Exception {
        connection.setAutoCommit(false);

        try {

            Konto afsenderKonto = getKontoExists(afsenderKontoNr);

            if (afsenderKonto.getSaldo() < beloeb) {
                throw new Exception("Du har ikke nok penge på kontoen.");
            }

            Bruger modtager = getBruger(modtagerMobilNr);
            if (modtager == null) {
                throw new Exception("Modtagerens bruger findes ikke.");
            }
            Konto modtagerKonto = modtager.getPrimaryKonto();
            if (modtagerKonto == null) {
                throw new Exception("Modtageren har ikke en primær konto.");
            }

            //Træk beløb fra afsenderens konto
            afsenderKonto.setSaldo(afsenderKonto.getSaldo() - beloeb);
            updateKonto(afsenderKonto);

            //Læg beløb til modtagerens konto
            modtagerKonto.setSaldo(modtagerKonto.getSaldo() + beloeb);
            updateKonto(modtagerKonto);

            connection.commit(); //Commit transaktion
        } catch (Exception e) {
            connection.rollback(); //Rollback i tilfælde af fejl
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public Transaktion tilfoejTransaktion(int afsenderId, int modtagerId, Double beloeb) throws SQLException {
        String sql = "INSERT INTO Transaktion (afsender_id, modtager_id, beloeb) VALUES (?, ?, ?) RETURNING *";
        Transaktion nyTransaktion = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, afsenderId);
            stmt.setInt(2, modtagerId);
            stmt.setDouble(3, beloeb);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int transaktionsId = rs.getInt("transaktions_id");
                String dato = rs.getString("dato");
                nyTransaktion = new Transaktion(transaktionsId, afsenderId, modtagerId, beloeb, dato);
                Session.getCurrentUser().tilfoejTransaktion(nyTransaktion);
            }
        } catch (SQLException e) {
            throw e;
        }

        return nyTransaktion;
    }

    public void updatePrimaryKonto(int kontoId, boolean isPrimary) throws SQLException {
        String sql = "UPDATE Konto SET is_primary = ? WHERE konto_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, isPrimary);
            stmt.setInt(2, kontoId);
            stmt.executeUpdate();
        }
    }

    private void updateKonto(Konto konto) throws SQLException {

        String sql = "UPDATE Konto SET saldo = ? WHERE konto_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, konto.getSaldo());
            stmt.setInt(2, konto.getKontoId());
            stmt.executeUpdate();
        }
    }
}


