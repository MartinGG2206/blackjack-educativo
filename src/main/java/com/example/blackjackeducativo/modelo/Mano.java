package com.example.blackjackeducativo.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class Mano {
    private final List<Carta> cartas;

    public Mano() {
        this.cartas = new ArrayList<>();
    }

    public Mano(List<Carta> cartas) {
        this.cartas = new ArrayList<>(cartas);
    }

    public void agregarCarta(Carta carta) {
        cartas.add(carta);
    }

    public Carta removerUltimaCarta() {
        if (cartas.isEmpty()) {
            return null;
        }
        return cartas.remove(cartas.size() - 1);
    }

    public void limpiar() {
        cartas.clear();
    }

    public List<Carta> getCartas() {
        return Collections.unmodifiableList(cartas);
    }

    public int getCantidadCartas() {
        return cartas.size();
    }

    public int getTotalDuro() {
        int total = 0;
        for (Carta carta : cartas) {
            total += carta.getValor().esAs() ? 1 : carta.getValorBlackjack();
        }
        return total;
    }

    public List<Integer> getTotalesPosibles() {
        int totalBase = 0;
        int ases = 0;

        for (Carta carta : cartas) {
            if (carta.getValor().esAs()) {
                ases++;
                totalBase += 1;
            } else {
                totalBase += carta.getValorBlackjack();
            }
        }

        Set<Integer> totales = new LinkedHashSet<>();
        for (int asesComoOnce = 0; asesComoOnce <= ases; asesComoOnce++) {
            totales.add(totalBase + (asesComoOnce * 10));
        }

        return new ArrayList<>(totales);
    }

    public int getMejorTotal() {
        if (cartas.isEmpty()) {
            return 0;
        }

        List<Integer> totales = getTotalesPosibles();
        int mejor = Integer.MIN_VALUE;
        int menorPasada = Integer.MAX_VALUE;

        for (int total : totales) {
            if (total <= 21 && total > mejor) {
                mejor = total;
            }
            if (total > 21 && total < menorPasada) {
                menorPasada = total;
            }
        }

        return mejor != Integer.MIN_VALUE ? mejor : menorPasada;
    }

    public boolean estaSuave() {
        int mejorTotal = getMejorTotal();
        if (mejorTotal > 21) {
            return false;
        }

        for (int total : getTotalesPosibles()) {
            if (total == mejorTotal && total != getTotalDuro()) {
                return true;
            }
        }
        return false;
    }

    public boolean estaPasada() {
        return !cartas.isEmpty() && getMejorTotal() > 21;
    }

    public boolean esBlackjackNatural() {
        return cartas.size() == 2 && getMejorTotal() == 21;
    }

    public boolean puedeDividir() {
        if (cartas.size() != 2) {
            return false;
        }

        Carta primera = cartas.get(0);
        Carta segunda = cartas.get(1);
        return primera.getValorBlackjack() == segunda.getValorBlackjack();
    }

    public String getDescripcionTotal() {
        if (cartas.isEmpty()) {
            return "Sin cartas";
        }

        int mejorTotal = getMejorTotal();
        if (mejorTotal > 21) {
            return mejorTotal + " (pasada)";
        }
        if (esBlackjackNatural()) {
            return "21 (blackjack)";
        }
        if (estaSuave()) {
            return mejorTotal + " (suave)";
        }
        return String.valueOf(mejorTotal);
    }
}
