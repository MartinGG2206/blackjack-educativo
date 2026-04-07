package com.example.blackjackeducativo.modelo;

public final class EntradaProbabilidad {
    private final ValorCarta valorCarta;
    private final int cartasRestantes;
    private final double probabilidad;
    private final int totalSiPide;
    private final boolean sePasa;
    private final boolean mejoraMano;

    public EntradaProbabilidad(ValorCarta valorCarta,
                               int cartasRestantes,
                               double probabilidad,
                               int totalSiPide,
                               boolean sePasa,
                               boolean mejoraMano) {
        this.valorCarta = valorCarta;
        this.cartasRestantes = cartasRestantes;
        this.probabilidad = probabilidad;
        this.totalSiPide = totalSiPide;
        this.sePasa = sePasa;
        this.mejoraMano = mejoraMano;
    }

    public ValorCarta getValorCarta() {
        return valorCarta;
    }

    public int getCartasRestantes() {
        return cartasRestantes;
    }

    public double getProbabilidad() {
        return probabilidad;
    }

    public int getTotalSiPide() {
        return totalSiPide;
    }

    public boolean isSePasa() {
        return sePasa;
    }

    public boolean isMejoraMano() {
        return mejoraMano;
    }
}
