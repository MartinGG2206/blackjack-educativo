package com.example.blackjackeducativo.ui;

import com.example.blackjackeducativo.modelo.EntradaProbabilidad;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.table.AbstractTableModel;

public final class ModeloTablaProbabilidades extends AbstractTableModel {
    private static final String[] COLUMNAS = {
            "Carta", "Restantes", "Probabilidad", "Total si pides", "Te pasas", "Mejora"
    };

    private final NumberFormat porcentaje = NumberFormat.getPercentInstance(new Locale("es", "CO"));
    private List<EntradaProbabilidad> entradas = new ArrayList<>();

    public ModeloTablaProbabilidades() {
        porcentaje.setMinimumFractionDigits(2);
        porcentaje.setMaximumFractionDigits(2);
    }

    public void setEntradas(List<EntradaProbabilidad> entradas) {
        this.entradas = new ArrayList<>(entradas);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return entradas.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNAS.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNAS[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        EntradaProbabilidad entrada = entradas.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> entrada.getValorCarta().getEtiqueta();
            case 1 -> entrada.getCartasRestantes();
            case 2 -> porcentaje.format(entrada.getProbabilidad());
            case 3 -> entrada.getTotalSiPide();
            case 4 -> entrada.isSePasa() ? "Si" : "No";
            case 5 -> entrada.isMejoraMano() ? "Si" : "No";
            default -> "";
        };
    }
}
