package com.example.mobilepayprojekt;

public class Bruger {
    private int brugerId;
    private String fnavn;
    private String enavn;
    private String mobilNr;
    private String adgangskode;

    public Bruger(int brugerId, String fnavn, String enavn, String mobilNr, String adgangskode) {
        this.brugerId = brugerId;
        this.fnavn = fnavn;
        this.enavn = enavn;
        this.mobilNr = mobilNr;
        this.adgangskode = adgangskode;
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
}
