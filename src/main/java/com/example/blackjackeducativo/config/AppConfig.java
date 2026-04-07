package com.example.blackjackeducativo.config;

import com.example.blackjackeducativo.servicio.HistorialResultadosService;
import com.example.blackjackeducativo.servicio.ServicioAnalisisBlackjack;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ServicioAnalisisBlackjack servicioAnalisisBlackjack() {
        return new ServicioAnalisisBlackjack();
    }

    @Bean
    public HistorialResultadosService historialResultadosService(
            @Value("${blackjack.historial.path:data/historial_resultados.csv}") String historialPath) {
        return new HistorialResultadosService(Path.of(historialPath));
    }
}
