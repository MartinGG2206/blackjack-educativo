package com.example.blackjackeducativo.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.blackjackeducativo.modelo.AccionRecomendada;
import com.example.blackjackeducativo.modelo.Carta;
import com.example.blackjackeducativo.modelo.EstadoRonda;
import com.example.blackjackeducativo.modelo.Palo;
import com.example.blackjackeducativo.modelo.RecomendacionEducativa;
import com.example.blackjackeducativo.modelo.ResultadoAnalisis;
import com.example.blackjackeducativo.modelo.ResultadoProbabilidades;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class HistorialResultadosServiceTest {

    @TempDir
    Path directorioTemporal;

    @Test
    void debeGuardarYCargarResultados() {
        HistorialResultadosService service = new HistorialResultadosService(directorioTemporal.resolve("historial.csv"));
        ResultadoAnalisis analisis = new ResultadoAnalisis(
                List.of(new Carta(com.example.blackjackeducativo.modelo.ValorCarta.DIEZ, Palo.CORAZONES),
                        new Carta(com.example.blackjackeducativo.modelo.ValorCarta.SIETE, Palo.PICAS)),
                new Carta(com.example.blackjackeducativo.modelo.ValorCarta.SEIS, Palo.DIAMANTES),
                List.of(new Carta(com.example.blackjackeducativo.modelo.ValorCarta.REINA, Palo.TREBOLES)),
                List.of(),
                List.of(),
                "17",
                "6♦",
                new ResultadoProbabilidades(List.of(), 42, 0.20, 0.40),
                new RecomendacionEducativa(AccionRecomendada.PLANTARSE, "Texto")
        );

        service.guardarRegistro(analisis, EstadoRonda.GANADA, "6♦, Q♣", "9♣, J♦", "10♥, 7♠, 6♦, Q♣, 9♣, J♦");

        assertEquals(true, Files.exists(service.getArchivoHistorial()));
        assertEquals(1, service.cargarRegistros().size());
        assertEquals("Ganada", service.cargarRegistros().get(0).getEstadoRonda().getEtiqueta());
        assertEquals("10♥, 7♠", service.cargarRegistros().get(0).getCartasJugador());
        assertEquals("6♦, Q♣", service.cargarRegistros().get(0).getCartasDealer());
        assertEquals("9♣, J♦", service.cargarRegistros().get(0).getCartasOtrosJugadores());
    }
}
