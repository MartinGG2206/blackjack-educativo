package com.example.blackjackeducativo.modelo;

public enum Palo {
    CORAZONES("Corazones", "\u2665", true),
    DIAMANTES("Diamantes", "\u2666", true),
    PICAS("Picas", "\u2660", false),
    TREBOLES("Treboles", "\u2663", false);

    private final String nombre;
    private final String simbolo;
    private final boolean rojo;

    Palo(String nombre, String simbolo, boolean rojo) {
        this.nombre = nombre;
        this.simbolo = simbolo;
        this.rojo = rojo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public boolean esRojo() {
        return rojo;
    }
}
