package com.example.mobilepayprojekt;

public class Transaktion {
    private int transaktionsId;
    private int afsenderId;
    private int modtagerId;
    private double beloeb;
    private String dato;

    public Transaktion(int transaktionsId, int afsenderId, int modtagerId, double beloeb, String dato) {
        this.transaktionsId = transaktionsId;
        this.afsenderId = afsenderId;
        this.modtagerId = modtagerId;
        this.beloeb = beloeb;
        this.dato = dato;
    }

    public int getTransaktionsId() {
        return transaktionsId;
    }

    public void setTransaktionsId(int transaktionsId) {
        this.transaktionsId = transaktionsId;
    }

    public int getAfsenderId() {
        return afsenderId;
    }

    public void setAfsenderId(int afsenderId) {
        this.afsenderId = afsenderId;
    }

    public int getModtagerId() {
        return modtagerId;
    }

    public void setModtagerId(int modtagerId) {
        this.modtagerId = modtagerId;
    }

    public double getBeloeb() {
        return beloeb;
    }

    public void setBeloeb(double beloeb) {
        this.beloeb = beloeb;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }
}
