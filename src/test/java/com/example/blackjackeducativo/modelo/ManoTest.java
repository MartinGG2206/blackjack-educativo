package com.example.blackjackeducativo.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ManoTest {

    @Test
    void debeValorarAsComoUnoUOnceSegunConvenga() {
        Mano mano = new Mano();
        mano.agregarCarta(new Carta(ValorCarta.AS));
        mano.agregarCarta(new Carta(ValorCarta.NUEVE));
        mano.agregarCarta(new Carta(ValorCarta.AS));

        assertEquals(21, mano.getMejorTotal());
        assertTrue(mano.estaSuave());
        assertEquals("21 (suave)", mano.getDescripcionTotal());
    }

    @Test
    void debeDetectarParejaDivisiblePorValorBlackjack() {
        Mano mano = new Mano();
        mano.agregarCarta(new Carta(ValorCarta.DIEZ));
        mano.agregarCarta(new Carta(ValorCarta.REY));

        assertTrue(mano.puedeDividir());
    }
}
