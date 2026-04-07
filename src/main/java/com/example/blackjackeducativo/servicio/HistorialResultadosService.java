package com.example.blackjackeducativo.servicio;

import com.example.blackjackeducativo.modelo.Carta;
import com.example.blackjackeducativo.modelo.EstadoRonda;
import com.example.blackjackeducativo.modelo.RegistroRonda;
import com.example.blackjackeducativo.modelo.ResultadoAnalisis;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class HistorialResultadosService {
    private static final String CABECERA = "fecha_hora;resultado;valor_jugador_b64;carta_dealer_b64;cartas_jugador_b64;cartas_otros_b64;cartas_usadas_b64;recomendacion_b64";
    private final Path archivoHistorial;

    public HistorialResultadosService() {
        this(Path.of("data", "historial_resultados.csv"));
    }

    public HistorialResultadosService(Path archivoHistorial) {
        this.archivoHistorial = archivoHistorial;
    }

    public List<RegistroRonda> cargarRegistros() {
        try {
            asegurarArchivo();
            List<String> lineas = Files.readAllLines(archivoHistorial, StandardCharsets.UTF_8);
            List<RegistroRonda> registros = new ArrayList<>();

            for (int i = 1; i < lineas.size(); i++) {
                String linea = lineas.get(i).trim();
                if (linea.isEmpty()) {
                    continue;
                }
                registros.add(parsearRegistro(linea));
            }

            registros.sort(Comparator.comparing(RegistroRonda::getFechaHora).reversed());
            return registros;
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo leer el historial de resultados.", ex);
        }
    }

    public RegistroRonda guardarRegistro(ResultadoAnalisis analisis,
                                         EstadoRonda estadoRonda,
                                         String cartasDealer,
                                         String cartasOtrosJugadores,
                                         String cartasUsadasTrasLaRonda) {
        RegistroRonda registro = new RegistroRonda(
                LocalDateTime.now(),
                estadoRonda,
                analisis.getValorJugador(),
                cartasDealer,
                analisis.getCartasJugador().stream().map(Carta::getEtiqueta).collect(Collectors.joining(", ")),
                cartasOtrosJugadores,
                cartasUsadasTrasLaRonda,
                analisis.getRecomendacion().getAccion().getEtiqueta()
        );

        try {
            asegurarArchivo();
            Files.writeString(
                    archivoHistorial,
                    serializarRegistro(registro) + System.lineSeparator(),
                    StandardCharsets.UTF_8,
                    java.nio.file.StandardOpenOption.APPEND
            );
            return registro;
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo guardar el resultado de la ronda.", ex);
        }
    }

    public Path getArchivoHistorial() {
        return archivoHistorial;
    }

    private void asegurarArchivo() throws IOException {
        Path directorio = archivoHistorial.getParent();
        if (directorio != null) {
            Files.createDirectories(directorio);
        }
        if (!Files.exists(archivoHistorial)) {
            Files.writeString(archivoHistorial, CABECERA + System.lineSeparator(), StandardCharsets.UTF_8);
        }
    }

    private RegistroRonda parsearRegistro(String linea) {
        String[] columnas = linea.split(";", -1);
        if (columnas.length != 8 && columnas.length != 6) {
            throw new IllegalStateException("Linea de historial invalida: " + linea);
        }

        if (columnas.length == 6) {
            return new RegistroRonda(
                    LocalDateTime.parse(columnas[0]),
                    EstadoRonda.valueOf(columnas[1]),
                    decodificar(columnas[2]),
                    decodificar(columnas[3]),
                    decodificar(columnas[4]),
                    "Sin registro",
                    decodificar(columnas[3]) + ", " + decodificar(columnas[4]),
                    decodificar(columnas[5])
            );
        }

        return new RegistroRonda(
                LocalDateTime.parse(columnas[0]),
                EstadoRonda.valueOf(columnas[1]),
                decodificar(columnas[2]),
                decodificar(columnas[3]),
                decodificar(columnas[4]),
                decodificar(columnas[5]),
                decodificar(columnas[6]),
                decodificar(columnas[7])
        );
    }

    private String serializarRegistro(RegistroRonda registro) {
        return String.join(";",
                registro.getFechaHora().toString(),
                registro.getEstadoRonda().name(),
                codificar(registro.getValorJugador()),
                codificar(registro.getCartasDealer()),
                codificar(registro.getCartasJugador()),
                codificar(registro.getCartasOtrosJugadores()),
                codificar(registro.getCartasUsadasTrasLaRonda()),
                codificar(registro.getRecomendacion())
        );
    }

    private String codificar(String texto) {
        return Base64.getEncoder().encodeToString(texto.getBytes(StandardCharsets.UTF_8));
    }

    private String decodificar(String texto) {
        return new String(Base64.getDecoder().decode(texto), StandardCharsets.UTF_8);
    }
}
