package com.ahorrapp.ahorrapp;

public class Combobox {

    private String texto;
    private String id;

    public Combobox (String texto,String id) {

        this.texto = texto;
        this.id = id;
    }

    public String get_texto() {
        return texto;
    }

    public String get_id() {
        return id;
    }
}