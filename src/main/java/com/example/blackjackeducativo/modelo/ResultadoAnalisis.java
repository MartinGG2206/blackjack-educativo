package com.example.blackjackeducativo.modelo;

import java.util.Collections;
import java.util.List;

public final class ResultadoAnalisis {
    private final List<Carta> cartasJugador;
    private final Carta cartaVisibleDealer;
    private final List<Carta> cartasDealerAdicionales;
    private final List<Carta> cartasOtrosJugadores;
    private final List<Carta> cartasVistas;
    private final String valorJugador;
    private final String valorDealer;
    private final ResultadoProbabilidades resultadoProbabilidades;
    private final RecomendacionEducativa recomendacion;

    public ResultadoAnalisis(List<Carta> cartasJugador,
                             Carta cartaVisibleDealer,
                             List<Carta> cartasDealerAdicionales,
                             List<Carta> cartasOtrosJugadores,
                             List<Carta> cartasVistas,
                             String valorJugador,
                             String valorDealer,
                             ResultadoProbabilidades resultadoProbabilidades,
                             RecomendacionEducativa recomendacion) {
        this.cartasJugador = List.copyOf(cartasJugador);
        this.cartaVisibleDealer = cartaVisibleDealer;
        this.cartasDealerAdicionales = List.copyOf(cartasDealerAdicionales);
        this.cartasOtrosJugadores = List.copyOf(cartasOtrosJugadores);
        this.cartasVistas = List.copyOf(cartasVistas);
        this.valorJugador = valorJugador;
        this.valorDealer = valorDealer;
        this.resultadoProbabilidades = resultadoProbabilidades;
        this.recomendacion = recomendacion;
    }

    public List<Carta> getCartasJugador() {
        return Collections.unmodifiableList(cartasJugador);
    }

    public Carta getCartaVisibleDealer() {
        return cartaVisibleDealer;
    }

    public List<Carta> getCartasDealerAdicionales() {
        return Collections.unmodifiableList(cartasDealerAdicionales);
    }

    public List<Carta> getCartasOtrosJugadores() {
        return Collections.unmodifiableList(cartasOtrosJugadores);
    }

    public List<Carta> getCartasVistas() {
        return Collections.unmodifiableList(cartasVistas);
    }

    public String getValorJugador() {
        return valorJugador;
    }

    public String getValorDealer() {
        return valorDealer;
    }

    public ResultadoProbabilidades getResultadoProbabilidades() {
        return resultadoProbabilidades;
    }

    public RecomendacionEducativa getRecomendacion() {
        return recomendacion;
    }
}
