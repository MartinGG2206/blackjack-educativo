package com.example.blackjackeducativo.ui;

import com.example.blackjackeducativo.modelo.RegistroRonda;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public final class ModeloTablaHistorial extends AbstractTableModel {
    private static final String[] COLUMNAS = {"Fecha", "Resultado", "Jugador", "Dealer", "Cartas", "Otros", "Sugerencia"};
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private List<RegistroRonda> registros = new ArrayList<>();

    public void setRegistros(List<RegistroRonda> registros) {
        this.registros = new ArrayList<>(registros);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return registros.size();
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
        RegistroRonda registro = registros.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> FORMATO.format(registro.getFechaHora());
            case 1 -> registro.getEstadoRonda().getEtiqueta();
            case 2 -> registro.getValorJugador();
            case 3 -> registro.getCartasDealer();
            case 4 -> registro.getCartasJugador();
            case 5 -> registro.getCartasOtrosJugadores();
            case 6 -> registro.getRecomendacion();
            default -> "";
        };
    }
}
