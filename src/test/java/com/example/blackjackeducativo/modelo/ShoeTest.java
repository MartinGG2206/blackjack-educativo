package com.example.blackjackeducativo.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ShoeTest {

    @Test
    void noDebePermitirConsumirDosVecesLaMismaCartaExactaEnUnMazo() {
        Shoe shoe = new Shoe(1);
        Carta sieteCorazones = new Carta(ValorCarta.SIETE, Palo.CORAZONES);

        shoe.consumirCarta(sieteCorazones);

        assertEquals(0, shoe.getCartasRestantes(ValorCarta.SIETE, Palo.CORAZONES));
        assertThrows(IllegalStateException.class, () -> shoe.consumirCarta(sieteCorazones));
    }
}
