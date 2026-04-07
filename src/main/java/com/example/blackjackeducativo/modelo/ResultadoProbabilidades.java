package com.example.blackjackeducativo.modelo;

import java.util.Collections;
import java.util.List;

public final class ResultadoProbabilidades {
    private final List<EntradaProbabilidad> entradas;
    private final int totalCartasRestantes;
    private final double probabilidadPasarse;
    private final double probabilidadMejorar;

    public ResultadoProbabilidades(List<EntradaProbabilidad> entradas,
                                   int totalCartasRestantes,
                                   double probabilidadPasarse,
                                   double probabilidadMejorar) {
        this.entradas = List.copyOf(entradas);
        this.totalCartasRestantes = totalCartasRestantes;
        this.probabilidadPasarse = probabilidadPasarse;
        this.probabilidadMejorar = probabilidadMejorar;
    }

    public List<EntradaProbabilidad> getEntradas() {
        return Collections.unmodifiableList(entradas);
    }

    public int getTotalCartasRestantes() {
        return totalCartasRestantes;
    }

    public double getProbabilidadPasarse() {
        return probabilidadPasarse;
    }

    public double getProbabilidadMejorar() {
        return probabilidadMejorar;
    }
}
