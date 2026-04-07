package com.example.blackjackeducativo.web;

import com.example.blackjackeducativo.modelo.Carta;
import com.example.blackjackeducativo.modelo.EstadoJuego;
import com.example.blackjackeducativo.modelo.EstadoRonda;
import com.example.blackjackeducativo.modelo.Palo;
import com.example.blackjackeducativo.modelo.RegistroRonda;
import com.example.blackjackeducativo.modelo.ResultadoAnalisis;
import com.example.blackjackeducativo.modelo.ValorCarta;
import com.example.blackjackeducativo.servicio.HistorialResultadosService;
import com.example.blackjackeducativo.servicio.ServicioAnalisisBlackjack;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes("gameSession")
public class BlackjackWebController {
    private static final DateTimeFormatter FECHA_HISTORIAL = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final ServicioAnalisisBlackjack servicioAnalisis;
    private final HistorialResultadosService historialResultadosService;

    public BlackjackWebController(ServicioAnalisisBlackjack servicioAnalisis,
                                  HistorialResultadosService historialResultadosService) {
        this.servicioAnalisis = servicioAnalisis;
        this.historialResultadosService = historialResultadosService;
    }

    @ModelAttribute("gameSession")
    public WebGameState gameSession() {
        return new WebGameState();
    }

    @GetMapping("/")
    public String index(@ModelAttribute("gameSession") WebGameState gameSession, Model model) {
        cargarPantalla(model, gameSession.getEstadoJuego());
        return "index";
    }

    @GetMapping("/health")
    @ResponseBody
    public String health() {
        return "ok";
    }

    @PostMapping("/cartas/{grupo}")
    public String agregarCarta(@PathVariable String grupo,
                               @RequestParam ValorCarta valor,
                               @RequestParam Palo palo,
                               @ModelAttribute("gameSession") WebGameState gameSession,
                               RedirectAttributes redirectAttributes) {
        EstadoJuego estadoJuego = gameSession.getEstadoJuego();
        try {
            Carta carta = new Carta(valor, palo);
            switch (grupo) {
                case "jugador" -> estadoJuego.agregarCartaJugador(carta);
                case "dealer-visible" -> estadoJuego.establecerCartaVisibleDealer(carta);
                case "dealer-adicional" -> estadoJuego.agregarCartaDealerAdicional(carta);
                case "otros" -> estadoJuego.agregarCartaOtrosJugadores(carta);
                case "vistas" -> estadoJuego.agregarCartaVista(carta);
                default -> throw new IllegalArgumentException("Grupo de cartas no soportado.");
            }
            redirectAttributes.addFlashAttribute("successMessage", "Carta registrada en " + etiquetaGrupo(grupo) + ".");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/cartas/{grupo}/deshacer")
    public String deshacer(@PathVariable String grupo,
                           @ModelAttribute("gameSession") WebGameState gameSession,
                           RedirectAttributes redirectAttributes) {
        EstadoJuego estadoJuego = gameSession.getEstadoJuego();
        try {
            switch (grupo) {
                case "jugador" -> estadoJuego.quitarUltimaCartaJugador();
                case "dealer-visible" -> estadoJuego.quitarCartaVisibleDealer();
                case "dealer-adicional" -> estadoJuego.quitarUltimaCartaDealerAdicional();
                case "otros" -> estadoJuego.quitarUltimaCartaOtrosJugadores();
                case "vistas" -> estadoJuego.quitarUltimaCartaVista();
                default -> throw new IllegalArgumentException("Grupo de cartas no soportado.");
            }
            redirectAttributes.addFlashAttribute("successMessage", "Se retiro la ultima carta de " + etiquetaGrupo(grupo) + ".");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/cartas/{grupo}/limpiar")
    public String limpiar(@PathVariable String grupo,
                          @ModelAttribute("gameSession") WebGameState gameSession,
                          RedirectAttributes redirectAttributes) {
        EstadoJuego estadoJuego = gameSession.getEstadoJuego();
        try {
            switch (grupo) {
                case "jugador" -> estadoJuego.limpiarJugador();
                case "dealer-visible" -> estadoJuego.quitarCartaVisibleDealer();
                case "dealer-adicional" -> estadoJuego.limpiarCartasDealerAdicionales();
                case "otros" -> estadoJuego.limpiarOtrosJugadores();
                case "vistas" -> estadoJuego.limpiarCartasVistas();
                default -> throw new IllegalArgumentException("Grupo de cartas no soportado.");
            }
            redirectAttributes.addFlashAttribute("successMessage", "Se limpio " + etiquetaGrupo(grupo) + ".");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/mesa/nueva")
    public String nuevaPartida(@ModelAttribute("gameSession") WebGameState gameSession,
                               RedirectAttributes redirectAttributes) {
        gameSession.getEstadoJuego().nuevaPartida();
        redirectAttributes.addFlashAttribute("successMessage", "La ronda actual paso a cartas vistas y la mesa quedo lista.");
        return "redirect:/";
    }

    @PostMapping("/mesa/reiniciar")
    public String reiniciarZapato(@RequestParam int numeroMazos,
                                  @ModelAttribute("gameSession") WebGameState gameSession,
                                  RedirectAttributes redirectAttributes) {
        try {
            gameSession.getEstadoJuego().reiniciarZapato(numeroMazos);
            redirectAttributes.addFlashAttribute("successMessage", "Se reinicio el zapato con " + numeroMazos + " mazo(s).");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/resultado/{estado}")
    public String registrarResultado(@PathVariable EstadoRonda estado,
                                     @ModelAttribute("gameSession") WebGameState gameSession,
                                     RedirectAttributes redirectAttributes) {
        EstadoJuego estadoJuego = gameSession.getEstadoJuego();
        try {
            if (!estadoJuego.hayDatosMinimosParaAnalisis()) {
                throw new IllegalStateException("Primero registra cartas del jugador y la carta visible del dealer.");
            }

            ResultadoAnalisis analisis = servicioAnalisis.analizar(estadoJuego);
            RegistroRonda registro = historialResultadosService.guardarRegistro(
                    analisis,
                    estado,
                    construirCartasDealer(analisis),
                    unirCartas(analisis.getCartasOtrosJugadores()),
                    construirCartasUsadasTrasLaRonda(analisis)
            );
            estadoJuego.nuevaPartida();
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Ronda " + registro.getEstadoRonda().getEtiqueta().toLowerCase(Locale.ROOT)
                            + " guardada en " + historialResultadosService.getArchivoHistorial().toAbsolutePath() + "."
            );
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/";
    }

    private void cargarPantalla(Model model, EstadoJuego estadoJuego) {
        ResultadoAnalisis analisis = servicioAnalisis.analizar(estadoJuego);
        List<RegistroRonda> historial;
        try {
            historial = historialResultadosService.cargarRegistros();
        } catch (Exception ex) {
            historial = Collections.emptyList();
            model.addAttribute("errorMessage", ex.getMessage());
        }

        model.addAttribute("estadoJuego", estadoJuego);
        model.addAttribute("analisis", analisis);
        model.addAttribute("historialVista", historial.stream()
                .map(this::mapearHistorial)
                .toList());
        model.addAttribute("valores", ValorCarta.values());
        model.addAttribute("palos", Palo.values());
        model.addAttribute("opcionesMazos", IntStream.rangeClosed(1, 8).boxed().toList());
        model.addAttribute("probabilidadPasarseLabel", porcentaje(analisis.getResultadoProbabilidades().getProbabilidadPasarse()));
        model.addAttribute("probabilidadMejorarLabel", porcentaje(analisis.getResultadoProbabilidades().getProbabilidadMejorar()));
        model.addAttribute("dealerCompleto", construirCartasDealer(analisis));
        model.addAttribute("cartasJugadorTexto", unirCartas(analisis.getCartasJugador()));
        model.addAttribute("cartasOtrosTexto", unirCartas(analisis.getCartasOtrosJugadores()));
        model.addAttribute("cartasVistasTexto", unirCartas(analisis.getCartasVistas()));
        model.addAttribute("historialArchivo", historialResultadosService.getArchivoHistorial().toAbsolutePath());
    }

    private HistorialItemView mapearHistorial(RegistroRonda registro) {
        return new HistorialItemView(
                FECHA_HISTORIAL.format(registro.getFechaHora()),
                registro.getEstadoRonda().getEtiqueta(),
                registro.getCartasJugador(),
                registro.getCartasDealer(),
                registro.getCartasOtrosJugadores(),
                registro.getRecomendacion()
        );
    }

    private String porcentaje(double valor) {
        NumberFormat formato = NumberFormat.getPercentInstance(new Locale("es", "CO"));
        formato.setMinimumFractionDigits(2);
        formato.setMaximumFractionDigits(2);
        return formato.format(valor);
    }

    private String etiquetaGrupo(String grupo) {
        return switch (grupo) {
            case "jugador" -> "tu mano";
            case "dealer-visible" -> "la carta visible del dealer";
            case "dealer-adicional" -> "las cartas adicionales del dealer";
            case "otros" -> "las cartas abiertas de otros jugadores";
            case "vistas" -> "las cartas vistas";
            default -> "la mesa";
        };
    }

    private String unirCartas(List<Carta> cartas) {
        if (cartas.isEmpty()) {
            return "Sin registro";
        }
        return cartas.stream().map(Carta::getEtiqueta).collect(Collectors.joining(", "));
    }

    private String construirCartasDealer(ResultadoAnalisis analisis) {
        StringBuilder dealer = new StringBuilder();
        if (analisis.getCartaVisibleDealer() != null) {
            dealer.append(analisis.getCartaVisibleDealer().getEtiqueta());
        }
        if (!analisis.getCartasDealerAdicionales().isEmpty()) {
            if (!dealer.isEmpty()) {
                dealer.append(", ");
            }
            dealer.append(analisis.getCartasDealerAdicionales().stream()
                    .map(Carta::getEtiqueta)
                    .collect(Collectors.joining(", ")));
        }
        return dealer.isEmpty() ? "Sin registro" : dealer.toString();
    }

    private String construirCartasUsadasTrasLaRonda(ResultadoAnalisis analisis) {
        StringBuilder acumulado = new StringBuilder();
        agregarBloque(acumulado, analisis.getCartasVistas());
        agregarBloque(acumulado, analisis.getCartasJugador());
        if (analisis.getCartaVisibleDealer() != null) {
            if (!acumulado.isEmpty()) {
                acumulado.append(", ");
            }
            acumulado.append(analisis.getCartaVisibleDealer().getEtiqueta());
        }
        agregarBloque(acumulado, analisis.getCartasDealerAdicionales());
        agregarBloque(acumulado, analisis.getCartasOtrosJugadores());
        return acumulado.isEmpty() ? "Sin registro" : acumulado.toString();
    }

    private void agregarBloque(StringBuilder acumulado, List<Carta> cartas) {
        if (cartas.isEmpty()) {
            return;
        }
        if (!acumulado.isEmpty()) {
            acumulado.append(", ");
        }
        acumulado.append(cartas.stream().map(Carta::getEtiqueta).collect(Collectors.joining(", ")));
    }

    public record HistorialItemView(
            String fechaHora,
            String estado,
            String cartasJugador,
            String cartasDealer,
            String cartasOtros,
            String recomendacion
    ) {
    }
}
