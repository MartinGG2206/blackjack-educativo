package com.example.blackjackeducativo.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.blackjackeducativo.modelo.EntradaProbabilidad;
import com.example.blackjackeducativo.modelo.EstadoJuego;
import com.example.blackjackeducativo.modelo.ResultadoProbabilidades;
import com.example.blackjackeducativo.modelo.ValorCarta;
import org.junit.jupiter.api.Test;

class CalculadoraProbabilidadesTest {

    @Test
    void debeCalcularRiesgoDePasarseYMejoraConConteoRestante() {
        EstadoJuego estadoJuego = new EstadoJuego(1);
        estadoJuego.agregarCartaJugador(ValorCarta.DIEZ);
        estadoJuego.agregarCartaJugador(ValorCarta.SEIS);
        estadoJuego.establecerCartaVisibleDealer(ValorCarta.CINCO);

        ResultadoProbabilidades resultado = new CalculadoraProbabilidades().calcular(estadoJuego);

        assertEquals(49, resultado.getTotalCartasRestantes());
        assertEquals(30d / 49d, resultado.getProbabilidadPasarse(), 0.000001);
        assertEquals(19d / 49d, resultado.getProbabilidadMejorar(), 0.000001);

        EntradaProbabilidad as = resultado.getEntradas().stream()
                .filter(entrada -> entrada.getValorCarta() == ValorCarta.AS)
                .findFirst()
                .orElseThrow();

        assertEquals(17, as.getTotalSiPide());
        assertFalse(as.isSePasa());
        assertTrue(as.isMejoraMano());
    }
}
