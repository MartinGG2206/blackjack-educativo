package com.example.blackjackeducativo.modelo;

public enum AccionRecomendada {
    PEDIR("Pedir"),
    PLANTARSE("Plantarse"),
    DOBLAR("Doblar"),
    DIVIDIR("Dividir");

    private final String etiqueta;

    AccionRecomendada(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
