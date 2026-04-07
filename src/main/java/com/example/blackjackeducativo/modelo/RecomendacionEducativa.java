package com.example.blackjackeducativo.modelo;

import java.util.Objects;

public final class RecomendacionEducativa {
    private final AccionRecomendada accion;
    private final String explicacion;

    public RecomendacionEducativa(AccionRecomendada accion, String explicacion) {
        this.accion = Objects.requireNonNull(accion, "La accion es obligatoria.");
        this.explicacion = Objects.requireNonNull(explicacion, "La explicacion es obligatoria.");
    }

    public AccionRecomendada getAccion() {
        return accion;
    }

    public String getExplicacion() {
        return explicacion;
    }
}
