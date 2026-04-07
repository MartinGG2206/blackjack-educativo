package com.example.blackjackeducativo.web;

import com.example.blackjackeducativo.modelo.EstadoJuego;

public class WebGameState {
    private final EstadoJuego estadoJuego = new EstadoJuego(6);

    public EstadoJuego getEstadoJuego() {
        return estadoJuego;
    }
}
