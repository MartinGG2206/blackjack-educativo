package com.example.blackjackeducativo.modelo;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public final class Shoe {
    private final EnumMap<ValorCarta, EnumMap<Palo, Integer>> conteoRestante;
    private int numeroMazos;

    public Shoe(int numeroMazos) {
        validarNumeroMazos(numeroMazos);
        this.conteoRestante = new EnumMap<>(ValorCarta.class);
        resetear(numeroMazos);
    }

    public void resetear(int numeroMazos) {
        validarNumeroMazos(numeroMazos);
        this.numeroMazos = numeroMazos;
        conteoRestante.clear();
        for (ValorCarta valor : ValorCarta.values()) {
            EnumMap<Palo, Integer> conteoPorPalo = new EnumMap<>(Palo.class);
            for (Palo palo : Palo.values()) {
                conteoPorPalo.put(palo, numeroMazos);
            }
            conteoRestante.put(valor, conteoPorPalo);
        }
    }

    public Carta consumirCarta(ValorCarta valor) {
        Palo paloDisponible = buscarPaloDisponible(valor);
        if (paloDisponible == null) {
            throw new IllegalStateException("No quedan cartas del valor " + valor.getEtiqueta() + " en el zapato.");
        }
        Carta carta = new Carta(valor, paloDisponible);
        consumirCarta(carta);
        return carta;
    }

    public void consumirCarta(Carta carta) {
        int restante = getCartasRestantes(carta.getValor(), carta.getPalo());
        if (restante <= 0) {
            throw new IllegalStateException("No queda disponible la carta " + carta.getEtiqueta() + " en el zapato.");
        }
        conteoRestante.get(carta.getValor()).put(carta.getPalo(), restante - 1);
    }

    public void devolverCarta(Carta carta) {
        int maximo = numeroMazos;
        int actual = getCartasRestantes(carta.getValor(), carta.getPalo());
        if (actual >= maximo) {
            throw new IllegalStateException("El conteo de " + carta.getEtiqueta() + " ya esta completo.");
        }
        conteoRestante.get(carta.getValor()).put(carta.getPalo(), actual + 1);
    }

    public int getCartasRestantes(ValorCarta valor) {
        EnumMap<Palo, Integer> conteoPorPalo = conteoRestante.get(valor);
        if (conteoPorPalo == null) {
            return 0;
        }
        return conteoPorPalo.values().stream().mapToInt(Integer::intValue).sum();
    }

    public int getCartasRestantes(ValorCarta valor, Palo palo) {
        EnumMap<Palo, Integer> conteoPorPalo = conteoRestante.get(valor);
        if (conteoPorPalo == null) {
            return 0;
        }
        return conteoPorPalo.getOrDefault(palo, 0);
    }

    public int getTotalCartasRestantes() {
        return conteoRestante.values().stream()
                .flatMap(mapa -> mapa.values().stream())
                .mapToInt(Integer::intValue)
                .sum();
    }

    public Map<ValorCarta, Integer> getConteoRestante() {
        EnumMap<ValorCarta, Integer> conteoAgrupado = new EnumMap<>(ValorCarta.class);
        for (ValorCarta valor : ValorCarta.values()) {
            conteoAgrupado.put(valor, getCartasRestantes(valor));
        }
        return Collections.unmodifiableMap(conteoAgrupado);
    }

    public int getNumeroMazos() {
        return numeroMazos;
    }

    private Palo buscarPaloDisponible(ValorCarta valor) {
        for (Palo palo : Palo.values()) {
            if (getCartasRestantes(valor, palo) > 0) {
                return palo;
            }
        }
        return null;
    }

    private void validarNumeroMazos(int numeroMazos) {
        if (numeroMazos < 1 || numeroMazos > 8) {
            throw new IllegalArgumentException("El numero de mazos debe estar entre 1 y 8.");
        }
    }
}
