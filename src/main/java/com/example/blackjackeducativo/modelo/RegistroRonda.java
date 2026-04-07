package com.example.blackjackeducativo.modelo;

import java.time.LocalDateTime;
import java.util.Objects;

public final class RegistroRonda {
    private final LocalDateTime fechaHora;
    private final EstadoRonda estadoRonda;
    private final String valorJugador;
    private final String cartasDealer;
    private final String cartasJugador;
    private final String cartasOtrosJugadores;
    private final String cartasUsadasTrasLaRonda;
    private final String recomendacion;

    public RegistroRonda(LocalDateTime fechaHora,
                         EstadoRonda estadoRonda,
                         String valorJugador,
                         String cartasDealer,
                         String cartasJugador,
                         String cartasOtrosJugadores,
                         String cartasUsadasTrasLaRonda,
                         String recomendacion) {
        this.fechaHora = Objects.requireNonNull(fechaHora, "La fecha es obligatoria.");
        this.estadoRonda = Objects.requireNonNull(estadoRonda, "El resultado es obligatorio.");
        this.valorJugador = Objects.requireNonNull(valorJugador, "El valor del jugador es obligatorio.");
        this.cartasDealer = Objects.requireNonNull(cartasDealer, "Las cartas del dealer son obligatorias.");
        this.cartasJugador = Objects.requireNonNull(cartasJugador, "Las cartas del jugador son obligatorias.");
        this.cartasOtrosJugadores = Objects.requireNonNull(cartasOtrosJugadores, "Las cartas de otros jugadores son obligatorias.");
        this.cartasUsadasTrasLaRonda = Objects.requireNonNull(cartasUsadasTrasLaRonda, "Las cartas usadas son obligatorias.");
        this.recomendacion = Objects.requireNonNull(recomendacion, "La recomendacion es obligatoria.");
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public EstadoRonda getEstadoRonda() {
        return estadoRonda;
    }

    public String getValorJugador() {
        return valorJugador;
    }

    public String getCartasDealer() {
        return cartasDealer;
    }

    public String getCartasJugador() {
        return cartasJugador;
    }

    public String getCartasOtrosJugadores() {
        return cartasOtrosJugadores;
    }

    public String getCartasUsadasTrasLaRonda() {
        return cartasUsadasTrasLaRonda;
    }

    public String getRecomendacion() {
        return recomendacion;
    }
}
