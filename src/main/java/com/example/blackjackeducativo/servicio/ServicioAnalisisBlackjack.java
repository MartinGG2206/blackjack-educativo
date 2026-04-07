package com.example.blackjackeducativo.servicio;

import com.example.blackjackeducativo.modelo.Carta;
import com.example.blackjackeducativo.modelo.EstadoJuego;
import com.example.blackjackeducativo.modelo.RecomendacionEducativa;
import com.example.blackjackeducativo.modelo.ResultadoAnalisis;
import com.example.blackjackeducativo.modelo.ResultadoProbabilidades;

public final class ServicioAnalisisBlackjack {
    private final CalculadoraProbabilidades calculadoraProbabilidades;
    private final RecomendadorEstrategia recomendadorEstrategia;

    public ServicioAnalisisBlackjack() {
        this.calculadoraProbabilidades = new CalculadoraProbabilidades();
        this.recomendadorEstrategia = new RecomendadorEstrategia();
    }

    public ResultadoAnalisis analizar(EstadoJuego estadoJuego) {
        ResultadoProbabilidades probabilidades = calculadoraProbabilidades.calcular(estadoJuego);
        RecomendacionEducativa recomendacion = recomendadorEstrategia.recomendar(estadoJuego, probabilidades);
        Carta dealer = estadoJuego.getCartaVisibleDealer();

        return new ResultadoAnalisis(
                estadoJuego.getManoJugador().getCartas(),
                dealer,
                estadoJuego.getCartasDealerAdicionales(),
                estadoJuego.getCartasOtrosJugadores(),
                estadoJuego.getCartasVistas(),
                estadoJuego.getManoJugador().getDescripcionTotal(),
                dealer == null ? "Sin carta visible" : dealer.getEtiqueta(),
                probabilidades,
                recomendacion
        );
    }
}
