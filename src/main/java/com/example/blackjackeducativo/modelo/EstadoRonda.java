package com.example.blackjackeducativo.modelo;

public enum EstadoRonda {
    GANADA("Ganada"),
    PERDIDA("Perdida"),
    EMPATE("Empate");

    private final String etiqueta;

    EstadoRonda(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
