package com.example.mobilepayprojekt;

import java.util.ArrayList;

public class Bruger {
    private int brugerId;
    private String fnavn;
    private String enavn;
    private String mobilNr;
    private String adgangskode;
    private ArrayList<Konto> konti;
    private ArrayList<Transaktion> transaktioner;

    public Bruger(int brugerId, String fnavn, String enavn, String mobilNr) {
        this.brugerId = brugerId;
        this.fnavn = fnavn;
        this.enavn = enavn;
        this.mobilNr = mobilNr;
    }

    public Bruger(int brugerId, String fnavn, String enavn, String mobilNr, String adgangskode) {
        this.brugerId = brugerId;
        this.fnavn = fnavn;
        this.enavn = enavn;
        this.mobilNr = mobilNr;
        this.adgangskode = adgangskode;
        this.konti = new ArrayList<>();
        this.transaktioner = new ArrayList<>();
    }

    public int getBrugerId() {
        return brugerId;
    }

    public void setBrugerId(int brugerId) {
        this.brugerId = brugerId;
    }

    public String getFnavn() {
        return fnavn;
    }

    public void setFnavn(String fnavn) {
        this.fnavn = fnavn;
    }

    public String getEnavn() {
        return enavn;
    }

    public void setEnavn(String enavn) {
        this.enavn = enavn;
    }

    public String getMobilNr() {
        return mobilNr;
    }

    public void setMobilNr(String mobilNr) {
        this.mobilNr = mobilNr;
    }

    public String getAdgangskode() {
        return adgangskode;
    }

    public void setAdgangskode(String adgangskode) {
        this.adgangskode = adgangskode;
    }

    public ArrayList<Konto> getKonti() {
        return konti;
    }

    public void setKonti(ArrayList<Konto> konti) {
        this.konti = konti;
    }

    public ArrayList<Transaktion> getTransaktioner() {
        return transaktioner;
    }

    public void setTransaktioner(ArrayList<Transaktion> transaktioner) {
        this.transaktioner = transaktioner;
    }

    public void tilfoejKonto(Konto konto, boolean isPrimary) {
        if (isPrimary) {
            for (Konto existingKonto : konti) {
                existingKonto.setPrimary(false);
            }
        }
        konto.setPrimary(isPrimary);
        this.konti.add(konto);
    }

    public Konto getPrimaryKonto() {
        for (Konto konto : konti) {
            if (konto.isPrimary()) {
                return konto;
            }
        }
        return null;
    }

    public void tilfoejTransaktion(Transaktion transaktion) {
        this.transaktioner.add(transaktion);
    }

    public void fjernKonto(int kontoId) {
        konti.removeIf(konto -> konto.getKontoId() == kontoId);
    }
}
