package com.example.blackjackeducativo.servicio;

import com.example.blackjackeducativo.modelo.Carta;
import com.example.blackjackeducativo.modelo.EntradaProbabilidad;
import com.example.blackjackeducativo.modelo.EstadoJuego;
import com.example.blackjackeducativo.modelo.Mano;
import com.example.blackjackeducativo.modelo.Palo;
import com.example.blackjackeducativo.modelo.ResultadoProbabilidades;
import com.example.blackjackeducativo.modelo.ValorCarta;
import java.util.ArrayList;
import java.util.List;

public final class CalculadoraProbabilidades {

    public ResultadoProbabilidades calcular(EstadoJuego estadoJuego) {
        Mano manoJugador = estadoJuego.getManoJugador();
        int totalActual = manoJugador.getMejorTotal();
        int totalCartasRestantes = estadoJuego.getShoe().getTotalCartasRestantes();
        List<EntradaProbabilidad> entradas = new ArrayList<>();

        if (totalCartasRestantes == 0) {
            return new ResultadoProbabilidades(entradas, 0, 0.0, 0.0);
        }

        double probabilidadPasarse = 0.0;
        double probabilidadMejorar = 0.0;

        for (ValorCarta valor : ValorCarta.values()) {
            int restantes = estadoJuego.getShoe().getCartasRestantes(valor);
            double probabilidad = restantes / (double) totalCartasRestantes;
            Mano manoSimulada = new Mano(manoJugador.getCartas());
            manoSimulada.agregarCarta(new Carta(valor, Palo.CORAZONES));
            int totalSimulado = manoSimulada.getMejorTotal();
            boolean sePasa = totalSimulado > 21;
            boolean mejora = !sePasa && totalSimulado > totalActual;

            if (sePasa) {
                probabilidadPasarse += probabilidad;
            }
            if (mejora) {
                probabilidadMejorar += probabilidad;
            }

            entradas.add(new EntradaProbabilidad(valor, restantes, probabilidad, totalSimulado, sePasa, mejora));
        }

        return new ResultadoProbabilidades(entradas, totalCartasRestantes, probabilidadPasarse, probabilidadMejorar);
    }
}
