package com.example.blackjackeducativo.ui;

import com.example.blackjackeducativo.modelo.Carta;
import com.example.blackjackeducativo.modelo.EstadoJuego;
import com.example.blackjackeducativo.modelo.EstadoRonda;
import com.example.blackjackeducativo.modelo.Palo;
import com.example.blackjackeducativo.modelo.RegistroRonda;
import com.example.blackjackeducativo.modelo.ResultadoAnalisis;
import com.example.blackjackeducativo.modelo.ResultadoProbabilidades;
import com.example.blackjackeducativo.modelo.ValorCarta;
import com.example.blackjackeducativo.servicio.HistorialResultadosService;
import com.example.blackjackeducativo.servicio.ServicioAnalisisBlackjack;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;

public final class VentanaPrincipal extends JFrame {
    private static final Color COLOR_FONDO = new Color(243, 245, 248);
    private static final Color COLOR_PANEL = Color.WHITE;
    private static final Color COLOR_JUGADOR = new Color(31, 122, 140);
    private static final Color COLOR_DEALER = new Color(200, 90, 53);
    private static final Color COLOR_OTROS = new Color(112, 82, 163);
    private static final Color COLOR_VISTAS = new Color(95, 140, 70);

    private final EstadoJuego estadoJuego;
    private final ServicioAnalisisBlackjack servicioAnalisis;
    private final HistorialResultadosService historialResultadosService;
    private final NumberFormat porcentaje;

    private final JSpinner selectorMazos;
    private final SelectorPaloPanel selectorPaloJugador;
    private final SelectorPaloPanel selectorPaloDealer;
    private final SelectorPaloPanel selectorPaloOtros;
    private final SelectorPaloPanel selectorPaloVistas;
    private final PanelCartasRegistradas panelCartasJugador;
    private final PanelCartasRegistradas panelCartasDealer;
    private final PanelCartasRegistradas panelCartasDealerAdicionales;
    private final PanelCartasRegistradas panelCartasOtros;
    private final PanelCartasRegistradas panelCartasVistas;

    private final JLabel etiquetaValorJugador;
    private final JLabel etiquetaValorDealer;
    private final JLabel etiquetaCartasRestantes;
    private final JLabel etiquetaProbabilidadPasarse;
    private final JLabel etiquetaProbabilidadMejorar;
    private final JLabel etiquetaAccion;
    private final JLabel etiquetaEstadoGuardado;
    private final JTextArea areaRecomendacion;
    private final ModeloTablaProbabilidades modeloTablaProbabilidades;
    private final ModeloTablaHistorial modeloTablaHistorial;

    public VentanaPrincipal() {
        super("Blackjack Educativo - Analisis Probabilistico");
        this.estadoJuego = new EstadoJuego(6);
        this.servicioAnalisis = new ServicioAnalisisBlackjack();
        this.historialResultadosService = new HistorialResultadosService();
        this.porcentaje = NumberFormat.getPercentInstance(new Locale("es", "CO"));
        this.porcentaje.setMinimumFractionDigits(2);
        this.porcentaje.setMaximumFractionDigits(2);

        this.selectorMazos = new JSpinner(new SpinnerNumberModel(6, 1, 8, 1));
        this.selectorPaloJugador = new SelectorPaloPanel("Palo:");
        this.selectorPaloDealer = new SelectorPaloPanel("Palo:");
        this.selectorPaloOtros = new SelectorPaloPanel("Palo:");
        this.selectorPaloVistas = new SelectorPaloPanel("Palo:");
        this.panelCartasJugador = new PanelCartasRegistradas("Haz clic en las cartas del jugador.", COLOR_JUGADOR);
        this.panelCartasDealer = new PanelCartasRegistradas("Selecciona la carta visible del dealer.", COLOR_DEALER);
        this.panelCartasDealerAdicionales = new PanelCartasRegistradas("Agrega las cartas finales del dealer.", COLOR_DEALER);
        this.panelCartasOtros = new PanelCartasRegistradas("Registra las cartas abiertas de otros jugadores.", COLOR_OTROS);
        this.panelCartasVistas = new PanelCartasRegistradas("Agrega cartas ya vistas del zapato.", COLOR_VISTAS);

        this.etiquetaValorJugador = new JLabel();
        this.etiquetaValorDealer = new JLabel();
        this.etiquetaCartasRestantes = new JLabel();
        this.etiquetaProbabilidadPasarse = new JLabel();
        this.etiquetaProbabilidadMejorar = new JLabel();
        this.etiquetaAccion = new JLabel();
        this.etiquetaEstadoGuardado = new JLabel("Historial: " + historialResultadosService.getArchivoHistorial().toAbsolutePath());
        this.areaRecomendacion = new JTextArea(7, 30);
        this.modeloTablaProbabilidades = new ModeloTablaProbabilidades();
        this.modeloTablaHistorial = new ModeloTablaHistorial();

        inicializarVentana();
        cargarHistorial();
        actualizarVista();
    }

    private void inicializarVentana() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1450, 930));
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout(14, 14));

        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearContenidoPrincipal(), BorderLayout.CENTER);
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 12));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 10, 0, 10));

        JLabel titulo = new JLabel("Mesa de captura");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 22f));

        JButton botonCalcular = crearBotonAccion("Actualizar analisis", new Color(37, 99, 235));
        botonCalcular.addActionListener(e -> actualizarVista());

        JButton botonNuevaPartida = crearBotonAccion("Nueva partida", new Color(15, 118, 110));
        botonNuevaPartida.addActionListener(e -> {
            estadoJuego.nuevaPartida();
            actualizarVista();
        });

        JButton botonReiniciarZapato = crearBotonAccion("Reiniciar zapato", new Color(107, 114, 128));
        botonReiniciarZapato.addActionListener(e -> {
            estadoJuego.reiniciarZapato((Integer) selectorMazos.getValue());
            actualizarVista();
        });

        panel.add(titulo);
        panel.add(Box.createHorizontalStrut(16));
        panel.add(new JLabel("Numero de mazos:"));
        panel.add(selectorMazos);
        panel.add(botonCalcular);
        panel.add(botonNuevaPartida);
        panel.add(botonReiniciarZapato);
        return panel;
    }

    private JSplitPane crearContenidoPrincipal() {
        JScrollPane entradas = new JScrollPane(crearPanelEntradas());
        entradas.setBorder(BorderFactory.createEmptyBorder());
        entradas.getVerticalScrollBar().setUnitIncrement(24);
        entradas.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        entradas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        entradas.setWheelScrollingEnabled(true);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, entradas, crearPanelResultados());
        splitPane.setDividerLocation(720);
        splitPane.setResizeWeight(0.54);
        splitPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        return splitPane;
    }

    private JPanel crearPanelEntradas() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_FONDO);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(crearCabeceraEntradas());
        panel.add(Box.createVerticalStrut(12));
        panel.add(crearPanelZona(
                "Tus cartas",
                "Selecciona el palo y luego el valor. El teclado se adapta al ancho y puedes deslizar si la ventana queda angosta.",
                COLOR_JUGADOR,
                selectorPaloJugador,
                panelCartasJugador,
                estadoJuego::agregarCartaJugador,
                estadoJuego::quitarUltimaCartaJugador,
                estadoJuego::limpiarJugador
        ));
        panel.add(Box.createVerticalStrut(12));
        panel.add(crearPanelZonaDealer());
        panel.add(Box.createVerticalStrut(12));
        panel.add(crearPanelZona(
                "Cartas abiertas de otros jugadores",
                "Estas cartas se descuentan del zapato y se guardan con palo exacto.",
                COLOR_OTROS,
                selectorPaloOtros,
                panelCartasOtros,
                estadoJuego::agregarCartaOtrosJugadores,
                estadoJuego::quitarUltimaCartaOtrosJugadores,
                estadoJuego::limpiarOtrosJugadores
        ));
        panel.add(Box.createVerticalStrut(12));
        panel.add(crearPanelZona(
                "Cartas ya vistas o usadas",
                "Sirve para simular un zapato avanzado sin volver a introducir todo desde cero.",
                COLOR_VISTAS,
                selectorPaloVistas,
                panelCartasVistas,
                estadoJuego::agregarCartaVista,
                estadoJuego::quitarUltimaCartaVista,
                estadoJuego::limpiarCartasVistas
        ));
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel crearCabeceraEntradas() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_PANEL);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 231)),
                BorderFactory.createEmptyBorder(14, 16, 14, 16))
        );

        JLabel titulo = new JLabel("Entrada directa por cartas reales");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel texto = new JLabel("<html>El zapato se controla por carta exacta. El panel de botones ahora usa ajuste automatico y scroll para evitar que se oculten cartas.</html>");
        texto.setForeground(new Color(79, 87, 99));
        texto.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(6));
        panel.add(texto);
        return panel;
    }

    private JPanel crearPanelZona(String titulo,
                                  String descripcion,
                                  Color colorAcento,
                                  SelectorPaloPanel selectorPalo,
                                  PanelCartasRegistradas panelCartas,
                                  Consumer<Carta> accionCarta,
                                  Runnable accionDeshacer,
                                  Runnable accionLimpiar) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorAcento, 2),
                BorderFactory.createEmptyBorder(14, 14, 14, 14))
        );

        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(tituloLabel.getFont().deriveFont(Font.BOLD, 18f));

        JLabel descripcionLabel = new JLabel("<html>" + descripcion + "</html>");
        descripcionLabel.setForeground(new Color(79, 87, 99));

        JPanel cabecera = new JPanel();
        cabecera.setBackground(COLOR_PANEL);
        cabecera.setLayout(new BoxLayout(cabecera, BoxLayout.Y_AXIS));
        cabecera.add(tituloLabel);
        cabecera.add(Box.createVerticalStrut(4));
        cabecera.add(descripcionLabel);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        acciones.setBackground(COLOR_PANEL);
        JButton botonDeshacer = crearBotonSecundario("Deshacer ultima");
        botonDeshacer.addActionListener(e -> {
            accionDeshacer.run();
            actualizarVista();
        });
        JButton botonLimpiar = crearBotonSecundario("Limpiar");
        botonLimpiar.addActionListener(e -> {
            accionLimpiar.run();
            actualizarVista();
        });
        acciones.add(botonDeshacer);
        acciones.add(botonLimpiar);

        JPanel superior = new JPanel(new BorderLayout());
        superior.setBackground(COLOR_PANEL);
        superior.add(cabecera, BorderLayout.CENTER);
        superior.add(acciones, BorderLayout.SOUTH);

        JPanel contenido = new JPanel(new BorderLayout(10, 10));
        contenido.setBackground(COLOR_PANEL);
        contenido.add(panelCartas, BorderLayout.NORTH);

        JPanel selectorYTeclado = new JPanel(new BorderLayout(8, 8));
        selectorYTeclado.setBackground(COLOR_PANEL);
        selectorYTeclado.add(selectorPalo, BorderLayout.NORTH);
        selectorYTeclado.add(crearContenedorTeclado(
                crearTecladoCartas(() -> selectorPalo.getPaloSeleccionado(), accionCarta, colorAcento)
        ), BorderLayout.CENTER);

        contenido.add(selectorYTeclado, BorderLayout.CENTER);
        panel.add(superior, BorderLayout.NORTH);
        panel.add(contenido, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelZonaDealer() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_DEALER, 2),
                BorderFactory.createEmptyBorder(14, 14, 14, 14))
        );

        JLabel titulo = new JLabel("Cartas del dealer");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));

        JLabel descripcion = new JLabel("<html>Registra la visible y despues las cartas adicionales del dealer. Todas cuentan como cartas usadas del zapato.</html>");
        descripcion.setForeground(new Color(79, 87, 99));

        JPanel cabecera = new JPanel();
        cabecera.setBackground(COLOR_PANEL);
        cabecera.setLayout(new BoxLayout(cabecera, BoxLayout.Y_AXIS));
        cabecera.add(titulo);
        cabecera.add(Box.createVerticalStrut(4));
        cabecera.add(descripcion);

        JButton botonQuitarUltima = crearBotonSecundario("Quitar ultima carta");
        botonQuitarUltima.addActionListener(e -> {
            estadoJuego.quitarUltimaCartaDealer();
            actualizarVista();
        });

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        acciones.setBackground(COLOR_PANEL);
        acciones.add(botonQuitarUltima);

        JPanel superior = new JPanel(new BorderLayout());
        superior.setBackground(COLOR_PANEL);
        superior.add(cabecera, BorderLayout.CENTER);
        superior.add(acciones, BorderLayout.SOUTH);

        JPanel contenido = new JPanel(new BorderLayout(10, 10));
        contenido.setBackground(COLOR_PANEL);
        JPanel selectorYTeclado = new JPanel();
        selectorYTeclado.setBackground(COLOR_PANEL);
        selectorYTeclado.setLayout(new BoxLayout(selectorYTeclado, BoxLayout.Y_AXIS));
        selectorYTeclado.add(selectorPaloDealer);
        selectorYTeclado.add(Box.createVerticalStrut(6));
        selectorYTeclado.add(new JLabel("Carta visible:"));
        selectorYTeclado.add(Box.createVerticalStrut(4));
        selectorYTeclado.add(panelCartasDealer);
        selectorYTeclado.add(Box.createVerticalStrut(6));
        selectorYTeclado.add(crearContenedorTeclado(
                crearTecladoCartas(() -> selectorPaloDealer.getPaloSeleccionado(), estadoJuego::establecerCartaVisibleDealer, COLOR_DEALER)
        ));
        selectorYTeclado.add(Box.createVerticalStrut(8));
        selectorYTeclado.add(new JLabel("Cartas adicionales del dealer:"));
        selectorYTeclado.add(Box.createVerticalStrut(4));
        selectorYTeclado.add(panelCartasDealerAdicionales);
        selectorYTeclado.add(Box.createVerticalStrut(6));
        selectorYTeclado.add(crearContenedorTeclado(
                crearTecladoCartas(() -> selectorPaloDealer.getPaloSeleccionado(), estadoJuego::agregarCartaDealerAdicional, COLOR_DEALER)
        ));

        contenido.add(selectorYTeclado, BorderLayout.CENTER);
        panel.add(superior, BorderLayout.NORTH);
        panel.add(contenido, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearTecladoCartas(Supplier<Palo> proveedorPalo, Consumer<Carta> accionCarta, Color colorAcento) {
        JPanel panel = new JPanel(new WrapLayout(FlowLayout.LEFT, 6, 6));
        panel.setBackground(COLOR_PANEL);

        for (ValorCarta valor : ValorCarta.values()) {
            panel.add(crearBotonCarta(valor, colorAcento, proveedorPalo, accionCarta));
        }
        return panel;
    }

    private JPanel crearContenedorTeclado(JPanel teclado) {
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(COLOR_PANEL);
        contenedor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8))
        );
        contenedor.add(teclado, BorderLayout.CENTER);
        return contenedor;
    }

    private JButton crearBotonCarta(ValorCarta valor,
                                    Color colorAcento,
                                    Supplier<Palo> proveedorPalo,
                                    Consumer<Carta> accionCarta) {
        JButton boton = new JButton(valor.getEtiqueta());
        boton.setFocusPainted(false);
        boton.setBackground(Color.WHITE);
        boton.setForeground(new Color(33, 37, 41));
        boton.setFont(boton.getFont().deriveFont(Font.BOLD, 13f));
        boton.setPreferredSize(new Dimension(56, 32));
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorAcento, 2),
                BorderFactory.createEmptyBorder(2, 4, 2, 4))
        );
        boton.addActionListener(e -> ejecutarAccionSegura(() -> accionCarta.accept(new Carta(valor, proveedorPalo.get()))));
        return boton;
    }

    private JButton crearBotonAccion(String texto, Color fondo) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(true);
        boton.setBackground(new Color(249, 250, 251));
        boton.setForeground(new Color(17, 24, 39));
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(fondo, 2),
                BorderFactory.createEmptyBorder(8, 12, 8, 12))
        );
        return boton;
    }

    private JButton crearBotonSecundario(String texto) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setBackground(new Color(245, 247, 250));
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219)),
                BorderFactory.createEmptyBorder(7, 10, 7, 10))
        );
        return boton;
    }

    private JPanel crearPanelResultados() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_FONDO);
        panel.add(crearPanelResumen(), BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, crearPanelProbabilidades(), crearPanelInferiorResultados());
        splitPane.setResizeWeight(0.45);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelResumen() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 10, 10));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createTitledBorder("Resumen del analisis"));
        panel.add(crearTarjetaResumen("Valor del jugador", etiquetaValorJugador));
        panel.add(crearTarjetaResumen("Carta visible del dealer", etiquetaValorDealer));
        panel.add(crearTarjetaResumen("Cartas restantes", etiquetaCartasRestantes));
        panel.add(crearTarjetaResumen("Probabilidad de pasarse", etiquetaProbabilidadPasarse));
        panel.add(crearTarjetaResumen("Probabilidad de mejorar", etiquetaProbabilidadMejorar));
        panel.add(crearTarjetaResumen("Accion sugerida", etiquetaAccion));
        return panel;
    }

    private JPanel crearTarjetaResumen(String titulo, JLabel etiquetaValor) {
        JPanel tarjeta = new JPanel();
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 213, 218)),
                BorderFactory.createEmptyBorder(12, 10, 12, 10))
        );

        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setAlignmentX(CENTER_ALIGNMENT);
        etiquetaValor.setAlignmentX(CENTER_ALIGNMENT);
        etiquetaValor.setFont(etiquetaValor.getFont().deriveFont(Font.BOLD, 19f));

        tarjeta.add(tituloLabel);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(etiquetaValor);
        return tarjeta;
    }

    private JScrollPane crearPanelProbabilidades() {
        JTable tabla = new JTable(modeloTablaProbabilidades);
        tabla.setRowHeight(26);
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Conteo restante y probabilidades por valor"));
        return scrollPane;
    }

    private JPanel crearPanelInferiorResultados() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_FONDO);
        panel.add(crearPanelRecomendacion(), BorderLayout.NORTH);
        panel.add(crearPanelHistorial(), BorderLayout.CENTER);
        panel.add(crearPanelRegistrarResultado(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelRecomendacion() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createTitledBorder("Recomendacion educativa"));
        areaRecomendacion.setEditable(false);
        areaRecomendacion.setLineWrap(true);
        areaRecomendacion.setWrapStyleWord(true);
        areaRecomendacion.setBackground(Color.WHITE);
        areaRecomendacion.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panel.add(new JScrollPane(areaRecomendacion), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelHistorial() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createTitledBorder("Historial guardado"));

        JTable tabla = new JTable(modeloTablaHistorial);
        tabla.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(tabla);
        panel.add(scrollPane, BorderLayout.CENTER);

        etiquetaEstadoGuardado.setForeground(new Color(79, 87, 99));
        panel.add(etiquetaEstadoGuardado, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelRegistrarResultado() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createTitledBorder("Guardar resultado de la ronda"));

        JButton botonGanada = crearBotonAccion("Marcar gane", new Color(21, 128, 61));
        botonGanada.addActionListener(e -> registrarResultado(EstadoRonda.GANADA));

        JButton botonPerdida = crearBotonAccion("Marcar perdi", new Color(185, 28, 28));
        botonPerdida.addActionListener(e -> registrarResultado(EstadoRonda.PERDIDA));

        JButton botonEmpate = crearBotonAccion("Marcar empate", new Color(180, 83, 9));
        botonEmpate.addActionListener(e -> registrarResultado(EstadoRonda.EMPATE));

        panel.add(botonGanada);
        panel.add(botonPerdida);
        panel.add(botonEmpate);
        return panel;
    }

    private void registrarResultado(EstadoRonda estadoRonda) {
        try {
            if (!estadoJuego.hayDatosMinimosParaAnalisis()) {
                JOptionPane.showMessageDialog(this, "Primero registra cartas del jugador y la carta visible del dealer.", "Ronda incompleta", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ResultadoAnalisis analisis = servicioAnalisis.analizar(estadoJuego);
            String cartasDealer = construirCartasDealer(analisis);
            String cartasOtrosJugadores = unirCartas(analisis.getCartasOtrosJugadores());
            String cartasUsadasTrasLaRonda = construirCartasUsadasTrasLaRonda(analisis);
            RegistroRonda registro = historialResultadosService.guardarRegistro(
                    analisis,
                    estadoRonda,
                    cartasDealer,
                    cartasOtrosJugadores,
                    cartasUsadasTrasLaRonda
            );
            estadoJuego.nuevaPartida();
            cargarHistorial();
            etiquetaEstadoGuardado.setText("Ultimo guardado: " + registro.getEstadoRonda().getEtiqueta()
                    + " | Archivo: " + historialResultadosService.getArchivoHistorial().toAbsolutePath());
            actualizarVista();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo guardar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarHistorial() {
        modeloTablaHistorial.setRegistros(historialResultadosService.cargarRegistros());
    }

    private void ejecutarAccionSegura(Runnable accion) {
        try {
            accion.run();
            actualizarVista();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Operacion no valida", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizarVista() {
        ResultadoAnalisis analisis = servicioAnalisis.analizar(estadoJuego);
        actualizarCartasEnMesa();
        actualizarResumen(analisis);
    }

    private void actualizarCartasEnMesa() {
        panelCartasJugador.mostrarCartas(estadoJuego.getManoJugador().getCartas());
        panelCartasDealer.mostrarCarta(estadoJuego.getCartaVisibleDealer());
        panelCartasDealerAdicionales.mostrarCartas(estadoJuego.getCartasDealerAdicionales());
        panelCartasOtros.mostrarCartas(estadoJuego.getCartasOtrosJugadores());
        panelCartasVistas.mostrarCartas(estadoJuego.getCartasVistas());
    }

    private void actualizarResumen(ResultadoAnalisis analisis) {
        ResultadoProbabilidades probabilidades = analisis.getResultadoProbabilidades();
        etiquetaValorJugador.setText(analisis.getValorJugador());
        etiquetaValorDealer.setText(analisis.getValorDealer());
        etiquetaCartasRestantes.setText(String.valueOf(probabilidades.getTotalCartasRestantes()));
        etiquetaProbabilidadPasarse.setText(porcentaje.format(probabilidades.getProbabilidadPasarse()));
        etiquetaProbabilidadMejorar.setText(porcentaje.format(probabilidades.getProbabilidadMejorar()));
        etiquetaAccion.setText(analisis.getRecomendacion().getAccion().getEtiqueta());
        areaRecomendacion.setText(analisis.getRecomendacion().getExplicacion() + "\n\n" + resumenCartasObservadas(analisis));
        modeloTablaProbabilidades.setEntradas(probabilidades.getEntradas());
    }

    private String resumenCartasObservadas(ResultadoAnalisis analisis) {
        return "Cartas del jugador: " + unirCartas(analisis.getCartasJugador())
                + "\nDealer visible: " + analisis.getValorDealer()
                + "\nDealer completo: " + construirCartasDealer(analisis)
                + "\nOtros jugadores: " + unirCartas(analisis.getCartasOtrosJugadores())
                + "\nCartas vistas acumuladas: " + unirCartas(analisis.getCartasVistas());
    }

    private String unirCartas(List<Carta> cartas) {
        if (cartas.isEmpty()) {
            return "Sin registro";
        }
        return cartas.stream().map(Carta::getEtiqueta).collect(Collectors.joining(", "));
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
}
