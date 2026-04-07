package com.example.blackjackeducativo.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.blackjackeducativo.modelo.AccionRecomendada;
import com.example.blackjackeducativo.modelo.EstadoJuego;
import com.example.blackjackeducativo.modelo.RecomendacionEducativa;
import com.example.blackjackeducativo.modelo.ResultadoProbabilidades;
import com.example.blackjackeducativo.modelo.ValorCarta;
import org.junit.jupiter.api.Test;

class RecomendadorEstrategiaTest {
    private final CalculadoraProbabilidades calculadora = new CalculadoraProbabilidades();
    private final RecomendadorEstrategia recomendador = new RecomendadorEstrategia();

    @Test
    void debeRecomendarPedirConDieciseisContraDiez() {
        EstadoJuego estadoJuego = new EstadoJuego(1);
        estadoJuego.agregarCartaJugador(ValorCarta.DIEZ);
        estadoJuego.agregarCartaJugador(ValorCarta.SEIS);
        estadoJuego.establecerCartaVisibleDealer(ValorCarta.DIEZ);

        ResultadoProbabilidades probabilidades = calculadora.calcular(estadoJuego);
        RecomendacionEducativa recomendacion = recomendador.recomendar(estadoJuego, probabilidades);

        assertEquals(AccionRecomendada.PEDIR, recomendacion.getAccion());
        assertTrue(recomendacion.getExplicacion().contains("probabilidad de pasarte"));
    }

    @Test
    void debeRecomendarDoblarConOnceContraSeis() {
        EstadoJuego estadoJuego = new EstadoJuego(1);
        estadoJuego.agregarCartaJugador(ValorCarta.CINCO);
        estadoJuego.agregarCartaJugador(ValorCarta.SEIS);
        estadoJuego.establecerCartaVisibleDealer(ValorCarta.SEIS);

        ResultadoProbabilidades probabilidades = calculadora.calcular(estadoJuego);
        RecomendacionEducativa recomendacion = recomendador.recomendar(estadoJuego, probabilidades);

        assertEquals(AccionRecomendada.DOBLAR, recomendacion.getAccion());
    }

    @Test
    void debeRecomendarDividirOchosContraDiez() {
        EstadoJuego estadoJuego = new EstadoJuego(1);
        estadoJuego.agregarCartaJugador(ValorCarta.OCHO);
        estadoJuego.agregarCartaJugador(ValorCarta.OCHO);
        estadoJuego.establecerCartaVisibleDealer(ValorCarta.DIEZ);

        ResultadoProbabilidades probabilidades = calculadora.calcular(estadoJuego);
        RecomendacionEducativa recomendacion = recomendador.recomendar(estadoJuego, probabilidades);

        assertEquals(AccionRecomendada.DIVIDIR, recomendacion.getAccion());
    }
}
