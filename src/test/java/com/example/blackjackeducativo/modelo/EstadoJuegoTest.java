package com.example.blackjackeducativo.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EstadoJuegoTest {

    @Test
    void nuevaPartidaDebeMoverTambienLasCartasAdicionalesDelDealer() {
        EstadoJuego estadoJuego = new EstadoJuego(1);
        estadoJuego.agregarCartaJugador(new Carta(ValorCarta.DIEZ, Palo.CORAZONES));
        estadoJuego.establecerCartaVisibleDealer(new Carta(ValorCarta.SEIS, Palo.PICAS));
        estadoJuego.agregarCartaDealerAdicional(new Carta(ValorCarta.REY, Palo.DIAMANTES));
        estadoJuego.agregarCartaOtrosJugadores(new Carta(ValorCarta.NUEVE, Palo.TREBOLES));

        estadoJuego.nuevaPartida();

        assertEquals(0, estadoJuego.getManoJugador().getCantidadCartas());
        assertEquals(0, estadoJuego.getCartasDealerAdicionales().size());
        assertEquals(4, estadoJuego.getCartasVistas().size());
    }

    @Test
    void quitarUltimaCartaDealerDebeQuitarPrimeroLaAdicionalYLuegoLaVisible() {
        EstadoJuego estadoJuego = new EstadoJuego(1);
        estadoJuego.establecerCartaVisibleDealer(new Carta(ValorCarta.CINCO, Palo.CORAZONES));
        estadoJuego.agregarCartaDealerAdicional(new Carta(ValorCarta.NUEVE, Palo.PICAS));

        estadoJuego.quitarUltimaCartaDealer();
        assertEquals(0, estadoJuego.getCartasDealerAdicionales().size());
        assertEquals("5♥", estadoJuego.getCartaVisibleDealer().getEtiqueta());

        estadoJuego.quitarUltimaCartaDealer();
        assertEquals(null, estadoJuego.getCartaVisibleDealer());
    }
}
