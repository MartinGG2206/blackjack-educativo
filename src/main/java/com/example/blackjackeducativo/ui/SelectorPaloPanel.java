package com.example.blackjackeducativo.ui;

import com.example.blackjackeducativo.modelo.Palo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public final class SelectorPaloPanel extends JPanel {
    private final JLabel etiquetaSeleccion;
    private final Map<Palo, JButton> botones;
    private Palo paloSeleccionado;

    public SelectorPaloPanel(String titulo) {
        this.botones = new EnumMap<>(Palo.class);
        this.paloSeleccionado = Palo.CORAZONES;
        setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
        setOpaque(false);

        add(new JLabel(titulo));
        for (Palo palo : Palo.values()) {
            JButton boton = new JButton(palo.getSimbolo());
            boton.setFocusPainted(false);
            boton.setPreferredSize(new Dimension(34, 22));
            boton.setToolTipText(palo.getNombre());
            boton.addActionListener(e -> seleccionarPalo(palo));
            botones.put(palo, boton);
            add(boton);
        }

        this.etiquetaSeleccion = new JLabel();
        add(etiquetaSeleccion);
        actualizarVista();
    }

    public Palo getPaloSeleccionado() {
        return paloSeleccionado;
    }

    private void seleccionarPalo(Palo palo) {
        this.paloSeleccionado = palo;
        actualizarVista();
    }

    private void actualizarVista() {
        for (Map.Entry<Palo, JButton> entrada : botones.entrySet()) {
            Palo palo = entrada.getKey();
            JButton boton = entrada.getValue();
            Color color = palo.esRojo() ? new Color(153, 27, 27) : new Color(31, 41, 55);
            if (palo == paloSeleccionado) {
                boton.setBackground(new Color(229, 231, 235));
                boton.setBorder(BorderFactory.createLineBorder(color, 2));
            } else {
                boton.setBackground(Color.WHITE);
                boton.setBorder(BorderFactory.createLineBorder(new Color(209, 213, 219)));
            }
            boton.setForeground(color);
        }

        etiquetaSeleccion.setText(" " + paloSeleccionado.getSimbolo());
        etiquetaSeleccion.setForeground(paloSeleccionado.esRojo() ? new Color(153, 27, 27) : new Color(31, 41, 55));
    }
}
