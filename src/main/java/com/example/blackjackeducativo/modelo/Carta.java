package com.example.blackjackeducativo.modelo;

import java.util.Objects;

public final class Carta {
    private final ValorCarta valor;
    private final Palo palo;

    public Carta(ValorCarta valor) {
        this(valor, Palo.CORAZONES);
    }

    public Carta(ValorCarta valor, Palo palo) {
        this.valor = Objects.requireNonNull(valor, "El valor de la carta es obligatorio.");
        this.palo = Objects.requireNonNull(palo, "El palo de la carta es obligatorio.");
    }

    public ValorCarta getValor() {
        return valor;
    }

    public Palo getPalo() {
        return palo;
    }

    public int getValorBlackjack() {
        return valor.getValorBlackjack();
    }

    public String getEtiqueta() {
        return valor.getEtiqueta() + palo.getSimbolo();
    }

    @Override
    public String toString() {
        return valor.getEtiqueta();
    }
}
