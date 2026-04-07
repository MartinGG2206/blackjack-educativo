package com.example.blackjackeducativo.modelo;

public enum ValorCarta {
    AS("As", 11),
    DOS("2", 2),
    TRES("3", 3),
    CUATRO("4", 4),
    CINCO("5", 5),
    SEIS("6", 6),
    SIETE("7", 7),
    OCHO("8", 8),
    NUEVE("9", 9),
    DIEZ("10", 10),
    JOTA("J", 10),
    REINA("Q", 10),
    REY("K", 10);

    private final String etiqueta;
    private final int valorBlackjack;

    ValorCarta(String etiqueta, int valorBlackjack) {
        this.etiqueta = etiqueta;
        this.valorBlackjack = valorBlackjack;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public int getValorBlackjack() {
        return valorBlackjack;
    }

    public boolean esAs() {
        return this == AS;
    }

    public boolean valeDiez() {
        return valorBlackjack == 10;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
