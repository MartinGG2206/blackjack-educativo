package com.example.blackjackeducativo.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class EstadoJuego {
    private int numeroMazos;
    private final Mano manoJugador;
    private final List<Carta> cartasDealerAdicionales;
    private final List<Carta> cartasOtrosJugadores;
    private final List<Carta> cartasVistas;
    private Shoe shoe;
    private Carta cartaVisibleDealer;

    public EstadoJuego(int numeroMazos) {
        this.numeroMazos = numeroMazos;
        this.shoe = new Shoe(numeroMazos);
        this.manoJugador = new Mano();
        this.cartasDealerAdicionales = new ArrayList<>();
        this.cartasOtrosJugadores = new ArrayList<>();
        this.cartasVistas = new ArrayList<>();
    }

    public void agregarCartaJugador(ValorCarta valor) {
        manoJugador.agregarCarta(shoe.consumirCarta(valor));
    }

    public void agregarCartaJugador(Carta carta) {
        shoe.consumirCarta(carta);
        manoJugador.agregarCarta(carta);
    }

    public void establecerCartaVisibleDealer(ValorCarta valor) {
        if (cartaVisibleDealer != null) {
            shoe.devolverCarta(cartaVisibleDealer);
        }
        cartaVisibleDealer = shoe.consumirCarta(valor);
    }

    public void establecerCartaVisibleDealer(Carta carta) {
        if (cartaVisibleDealer != null) {
            shoe.devolverCarta(cartaVisibleDealer);
        }
        shoe.consumirCarta(carta);
        cartaVisibleDealer = carta;
    }

    public void agregarCartaDealerAdicional(ValorCarta valor) {
        cartasDealerAdicionales.add(shoe.consumirCarta(valor));
    }

    public void agregarCartaDealerAdicional(Carta carta) {
        shoe.consumirCarta(carta);
        cartasDealerAdicionales.add(carta);
    }

    public void agregarCartaOtrosJugadores(ValorCarta valor) {
        cartasOtrosJugadores.add(shoe.consumirCarta(valor));
    }

    public void agregarCartaOtrosJugadores(Carta carta) {
        shoe.consumirCarta(carta);
        cartasOtrosJugadores.add(carta);
    }

    public void agregarCartaVista(ValorCarta valor) {
        cartasVistas.add(shoe.consumirCarta(valor));
    }

    public void agregarCartaVista(Carta carta) {
        shoe.consumirCarta(carta);
        cartasVistas.add(carta);
    }

    public void quitarUltimaCartaJugador() {
        Carta carta = manoJugador.removerUltimaCarta();
        if (carta != null) {
            shoe.devolverCarta(carta);
        }
    }

    public void quitarUltimaCartaOtrosJugadores() {
        quitarUltimaCarta(cartasOtrosJugadores);
    }

    public void quitarUltimaCartaDealerAdicional() {
        quitarUltimaCarta(cartasDealerAdicionales);
    }

    public void quitarUltimaCartaDealer() {
        if (!cartasDealerAdicionales.isEmpty()) {
            quitarUltimaCarta(cartasDealerAdicionales);
            return;
        }
        if (cartaVisibleDealer != null) {
            shoe.devolverCarta(cartaVisibleDealer);
            cartaVisibleDealer = null;
        }
    }

    public void quitarCartaVisibleDealer() {
        if (cartaVisibleDealer != null) {
            shoe.devolverCarta(cartaVisibleDealer);
            cartaVisibleDealer = null;
        }
    }

    public void quitarUltimaCartaVista() {
        quitarUltimaCarta(cartasVistas);
    }

    public void limpiarJugador() {
        for (Carta carta : manoJugador.getCartas()) {
            shoe.devolverCarta(carta);
        }
        manoJugador.limpiar();
    }

    public void limpiarDealer() {
        quitarCartaVisibleDealer();
        limpiarCartasDealerAdicionales();
    }

    public void limpiarCartasDealerAdicionales() {
        for (Carta carta : cartasDealerAdicionales) {
            shoe.devolverCarta(carta);
        }
        cartasDealerAdicionales.clear();
    }

    public void limpiarOtrosJugadores() {
        for (Carta carta : cartasOtrosJugadores) {
            shoe.devolverCarta(carta);
        }
        cartasOtrosJugadores.clear();
    }

    public void limpiarCartasVistas() {
        for (Carta carta : cartasVistas) {
            shoe.devolverCarta(carta);
        }
        cartasVistas.clear();
    }

    public void nuevaPartida() {
        cartasVistas.addAll(manoJugador.getCartas());
        if (cartaVisibleDealer != null) {
            cartasVistas.add(cartaVisibleDealer);
        }
        cartasVistas.addAll(cartasDealerAdicionales);
        cartasVistas.addAll(cartasOtrosJugadores);

        manoJugador.limpiar();
        cartaVisibleDealer = null;
        cartasDealerAdicionales.clear();
        cartasOtrosJugadores.clear();
    }

    public void reiniciarZapato(int numeroMazos) {
        this.numeroMazos = numeroMazos;
        this.shoe = new Shoe(numeroMazos);
        this.manoJugador.limpiar();
        this.cartaVisibleDealer = null;
        this.cartasDealerAdicionales.clear();
        this.cartasOtrosJugadores.clear();
        this.cartasVistas.clear();
    }

    public boolean hayDatosMinimosParaAnalisis() {
        return !manoJugador.getCartas().isEmpty() && cartaVisibleDealer != null;
    }

    public int getNumeroMazos() {
        return numeroMazos;
    }

    public Mano getManoJugador() {
        return manoJugador;
    }

    public Carta getCartaVisibleDealer() {
        return cartaVisibleDealer;
    }

    public List<Carta> getCartasOtrosJugadores() {
        return Collections.unmodifiableList(cartasOtrosJugadores);
    }

    public List<Carta> getCartasDealerAdicionales() {
        return Collections.unmodifiableList(cartasDealerAdicionales);
    }

    public List<Carta> getCartasVistas() {
        return Collections.unmodifiableList(cartasVistas);
    }

    public Shoe getShoe() {
        return shoe;
    }

    private void quitarUltimaCarta(List<Carta> cartas) {
        if (cartas.isEmpty()) {
            return;
        }
        Carta ultimaCarta = cartas.remove(cartas.size() - 1);
        shoe.devolverCarta(ultimaCarta);
    }
}
