package com.example.mobilepayprojekt;

public class Konto {

    private int kontoId;
    private int brugerId;
    private String kontoNr;
    private double saldo;
    private boolean isPrimary;

    public Konto(int kontoId, int brugerId, String kontoNr, double saldo, boolean isPrimary) {
        this.kontoId = kontoId;
        this.brugerId = brugerId;
        this.kontoNr = kontoNr;
        this.saldo = saldo;
        this.isPrimary = false;
    }

    public int getKontoId() {
        return kontoId;
    }

    public void setKontoId(int kontoId) {
        this.kontoId = kontoId;
    }

    public int getBrugerId() {
        return brugerId;
    }

    public void setBrugerId(int brugerId) {
        this.brugerId = brugerId;
    }

    public String getKontoNr() {
        return kontoNr;
    }

    public void setKontoNr(String kontoNr) {
        this.kontoNr = kontoNr;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void setPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public boolean isPrimary() {
        return this.isPrimary;
    }


}
