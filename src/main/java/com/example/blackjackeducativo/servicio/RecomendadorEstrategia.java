package com.example.blackjackeducativo.servicio;

import com.example.blackjackeducativo.modelo.AccionRecomendada;
import com.example.blackjackeducativo.modelo.Carta;
import com.example.blackjackeducativo.modelo.EstadoJuego;
import com.example.blackjackeducativo.modelo.Mano;
import com.example.blackjackeducativo.modelo.RecomendacionEducativa;
import com.example.blackjackeducativo.modelo.ResultadoProbabilidades;
import com.example.blackjackeducativo.modelo.ValorCarta;
import java.text.NumberFormat;
import java.util.Locale;

public final class RecomendadorEstrategia {
    private final NumberFormat porcentaje = NumberFormat.getPercentInstance(new Locale("es", "CO"));

    public RecomendadorEstrategia() {
        porcentaje.setMinimumFractionDigits(2);
        porcentaje.setMaximumFractionDigits(2);
    }

    public RecomendacionEducativa recomendar(EstadoJuego estadoJuego, ResultadoProbabilidades probabilidades) {
        if (!estadoJuego.hayDatosMinimosParaAnalisis()) {
            return new RecomendacionEducativa(
                    AccionRecomendada.PEDIR,
                    "Primero registra al menos una carta del jugador y la carta visible del dealer para generar una recomendacion educativa."
            );
        }

        Mano manoJugador = estadoJuego.getManoJugador();
        Carta dealer = estadoJuego.getCartaVisibleDealer();
        int valorDealer = valorEstrategicoDealer(dealer.getValor());
        int totalJugador = manoJugador.getMejorTotal();

        AccionRecomendada accionBase = resolverAccionBase(manoJugador, valorDealer);
        AccionRecomendada accionFinal = normalizarAccionSegunRestricciones(accionBase, manoJugador, valorDealer);

        String explicacion = construirExplicacion(manoJugador, dealer, probabilidades, accionFinal, accionBase, totalJugador);
        return new RecomendacionEducativa(accionFinal, explicacion);
    }

    private AccionRecomendada resolverAccionBase(Mano manoJugador, int valorDealer) {
        if (manoJugador.puedeDividir()) {
            return recomendarParejas(manoJugador, valorDealer);
        }
        if (manoJugador.estaSuave() && manoJugador.getCantidadCartas() == 2) {
            return recomendarSuaves(manoJugador.getMejorTotal(), valorDealer);
        }
        return recomendarDuras(manoJugador.getMejorTotal(), valorDealer);
    }

    private AccionRecomendada recomendarParejas(Mano manoJugador, int valorDealer) {
        int valorPareja = manoJugador.getCartas().get(0).getValorBlackjack();
        return switch (valorPareja) {
            case 11 -> AccionRecomendada.DIVIDIR;
            case 10 -> AccionRecomendada.PLANTARSE;
            case 9 -> (valorDealer >= 2 && valorDealer <= 6) || valorDealer == 8 || valorDealer == 9
                    ? AccionRecomendada.DIVIDIR : AccionRecomendada.PLANTARSE;
            case 8 -> AccionRecomendada.DIVIDIR;
            case 7 -> valorDealer <= 7 ? AccionRecomendada.DIVIDIR : AccionRecomendada.PEDIR;
            case 6 -> valorDealer >= 2 && valorDealer <= 6 ? AccionRecomendada.DIVIDIR : AccionRecomendada.PEDIR;
            case 5 -> valorDealer >= 2 && valorDealer <= 9 ? AccionRecomendada.DOBLAR : AccionRecomendada.PEDIR;
            case 4 -> valorDealer == 5 || valorDealer == 6 ? AccionRecomendada.DIVIDIR : AccionRecomendada.PEDIR;
            case 3, 2 -> valorDealer >= 2 && valorDealer <= 7 ? AccionRecomendada.DIVIDIR : AccionRecomendada.PEDIR;
            default -> AccionRecomendada.PEDIR;
        };
    }

    private AccionRecomendada recomendarSuaves(int totalJugador, int valorDealer) {
        return switch (totalJugador) {
            case 13, 14 -> valorDealer == 5 || valorDealer == 6 ? AccionRecomendada.DOBLAR : AccionRecomendada.PEDIR;
            case 15, 16 -> valorDealer >= 4 && valorDealer <= 6 ? AccionRecomendada.DOBLAR : AccionRecomendada.PEDIR;
            case 17 -> valorDealer >= 3 && valorDealer <= 6 ? AccionRecomendada.DOBLAR : AccionRecomendada.PEDIR;
            case 18 -> {
                if (valorDealer >= 3 && valorDealer <= 6) {
                    yield AccionRecomendada.DOBLAR;
                }
                if (valorDealer == 2 || valorDealer == 7 || valorDealer == 8) {
                    yield AccionRecomendada.PLANTARSE;
                }
                yield AccionRecomendada.PEDIR;
            }
            default -> AccionRecomendada.PLANTARSE;
        };
    }

    private AccionRecomendada recomendarDuras(int totalJugador, int valorDealer) {
        if (totalJugador <= 8) {
            return AccionRecomendada.PEDIR;
        }
        if (totalJugador == 9) {
            return valorDealer >= 3 && valorDealer <= 6 ? AccionRecomendada.DOBLAR : AccionRecomendada.PEDIR;
        }
        if (totalJugador == 10) {
            return valorDealer >= 2 && valorDealer <= 9 ? AccionRecomendada.DOBLAR : AccionRecomendada.PEDIR;
        }
        if (totalJugador == 11) {
            return valorDealer == 11 ? AccionRecomendada.PEDIR : AccionRecomendada.DOBLAR;
        }
        if (totalJugador == 12) {
            return valorDealer >= 4 && valorDealer <= 6 ? AccionRecomendada.PLANTARSE : AccionRecomendada.PEDIR;
        }
        if (totalJugador >= 13 && totalJugador <= 16) {
            return valorDealer >= 2 && valorDealer <= 6 ? AccionRecomendada.PLANTARSE : AccionRecomendada.PEDIR;
        }
        return AccionRecomendada.PLANTARSE;
    }

    private AccionRecomendada normalizarAccionSegunRestricciones(AccionRecomendada accionBase, Mano manoJugador, int valorDealer) {
        if (accionBase == AccionRecomendada.DIVIDIR && !manoJugador.puedeDividir()) {
            return manoJugador.getMejorTotal() >= 17 ? AccionRecomendada.PLANTARSE : AccionRecomendada.PEDIR;
        }
        if (accionBase == AccionRecomendada.DOBLAR && manoJugador.getCantidadCartas() != 2) {
            return valorDealer >= 2 && valorDealer <= 6 ? AccionRecomendada.PLANTARSE : AccionRecomendada.PEDIR;
        }
        return accionBase;
    }

    private String construirExplicacion(Mano manoJugador,
                                        Carta dealer,
                                        ResultadoProbabilidades probabilidades,
                                        AccionRecomendada accionFinal,
                                        AccionRecomendada accionBase,
                                        int totalJugador) {
        StringBuilder texto = new StringBuilder();
        texto.append("Recomendacion educativa: ").append(accionFinal.getEtiqueta()).append(". ");

        if (manoJugador.puedeDividir()) {
            texto.append("Tienes una pareja de ")
                    .append(manoJugador.getCartas().get(0).getEtiqueta())
                    .append(" frente a ")
                    .append(dealer.getEtiqueta())
                    .append(" del dealer. ");
        } else {
            texto.append("Tu mano vale ")
                    .append(manoJugador.getDescripcionTotal())
                    .append(" frente a ")
                    .append(dealer.getEtiqueta())
                    .append(" del dealer. ");
        }

        texto.append("Segun estrategia basica teorica, la linea base es ")
                .append(accionBase.getEtiqueta().toLowerCase(Locale.ROOT))
                .append(". ");

        texto.append("Con el zapato restante, la probabilidad de pasarte si pides es ")
                .append(porcentaje.format(probabilidades.getProbabilidadPasarse()))
                .append(" y la probabilidad de mejorar tu total es ")
                .append(porcentaje.format(probabilidades.getProbabilidadMejorar()))
                .append(". ");

        if (accionFinal == AccionRecomendada.PEDIR) {
            if (probabilidades.getProbabilidadMejorar() >= probabilidades.getProbabilidadPasarse() || totalJugador <= 11) {
                texto.append("Conviene pedir porque aun tienes bastante margen para sumar sin romperte, y el dealer muestra una carta que suele presionar a buscar mas valor.");
            } else {
                texto.append("Aunque el riesgo inmediato es alto, pedir sigue siendo la opcion teorica porque plantarte con este total suele dejar ventaja al dealer.");
            }
        } else if (accionFinal == AccionRecomendada.PLANTARSE) {
            texto.append("Plantarse evita exponer una mano ya razonable a un riesgo de ruptura que, en este contexto, pesa mas que la posible mejora.");
        } else if (accionFinal == AccionRecomendada.DOBLAR) {
            texto.append("Doblar es didacticamente razonable porque la estrategia basica aprovecha una ventaja inicial fuerte y el riesgo de pasarte aun es controlable con una sola carta.");
        } else {
            texto.append("Dividir crea dos manos con mejor expectativa teorica que mantener la pareja original, especialmente contra la carta visible del dealer.");
        }

        return texto.toString();
    }

    private int valorEstrategicoDealer(ValorCarta valor) {
        return valor == ValorCarta.AS ? 11 : Math.min(valor.getValorBlackjack(), 10);
    }
}
