package com.example.blackjackeducativo.ui;

import com.example.blackjackeducativo.modelo.Carta;
import com.example.blackjackeducativo.modelo.Palo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public final class PanelCartasRegistradas extends JPanel {
    private final String textoVacio;
    private final Color colorAcento;

    public PanelCartasRegistradas(String textoVacio, Color colorAcento) {
        this.textoVacio = textoVacio;
        this.colorAcento = colorAcento;
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setBackground(new Color(249, 250, 251));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(218, 220, 224)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8))
        );
    }

    public void mostrarCartas(List<Carta> cartas) {
        removeAll();
        if (cartas.isEmpty()) {
            add(crearEtiquetaVacia());
        } else {
            for (Carta carta : cartas) {
                add(crearTarjetaCarta(carta));
            }
        }
        revalidate();
        repaint();
    }

    public void mostrarCarta(Carta carta) {
        removeAll();
        add(carta == null ? crearEtiquetaVacia() : crearTarjetaCarta(carta));
        revalidate();
        repaint();
    }

    private JLabel crearEtiquetaVacia() {
        JLabel etiqueta = new JLabel(textoVacio);
        etiqueta.setForeground(new Color(100, 107, 118));
        return etiqueta;
    }

    private JPanel crearTarjetaCarta(Carta carta) {
        Palo palo = carta.getPalo();
        JPanel tarjeta = new JPanel();
        tarjeta.setOpaque(true);
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setPreferredSize(new Dimension(66, 88));
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorAcento, 2),
                BorderFactory.createEmptyBorder(8, 6, 8, 6))
        );

        JLabel esquinaSuperior = new JLabel(palo.getSimbolo(), SwingConstants.LEFT);
        esquinaSuperior.setForeground(colorPalo(palo));
        esquinaSuperior.setAlignmentX(LEFT_ALIGNMENT);

        JLabel valorLabel = new JLabel(carta.getValor().getEtiqueta(), SwingConstants.CENTER);
        valorLabel.setFont(valorLabel.getFont().deriveFont(Font.BOLD, 24f));
        valorLabel.setForeground(colorPalo(palo));
        valorLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel esquinaInferior = new JLabel(palo.getSimbolo(), SwingConstants.RIGHT);
        esquinaInferior.setForeground(colorPalo(palo));
        esquinaInferior.setAlignmentX(RIGHT_ALIGNMENT);

        tarjeta.add(esquinaSuperior);
        tarjeta.add(Box.createVerticalGlue());
        tarjeta.add(valorLabel);
        tarjeta.add(Box.createVerticalGlue());
        tarjeta.add(esquinaInferior);
        return tarjeta;
    }

    private Color colorPalo(Palo palo) {
        return palo.esRojo() ? new Color(185, 28, 28) : new Color(31, 41, 55);
    }
}
