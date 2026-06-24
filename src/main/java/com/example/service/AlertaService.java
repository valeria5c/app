// src/main/java/com/contaminacion/service/AlertaService.java
package com.example.service;

import com.example.model.Zona;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertaService {

    private final ZonaService zonaService;

    public List<Zona> obtenerZonasEnAlerta() {
        return zonaService.listarTodas().stream()
                .filter(z -> z.getPrediccion() != null)
                .filter(z -> !"VERDE".equals(z.getPrediccion().getNivelAlerta()))
                .collect(Collectors.toList());
    }

    public List<Zona> obtenerZonasCriticas() {
        return zonaService.buscarZonasCriticas();
    }

    public String generarResumenAlertas() {
        List<Zona> zonas = zonaService.listarTodas();
        long total = zonas.size();
        long criticas = zonas.stream()
                .filter(z -> z.getPrediccion() != null)
                .filter(z -> "CRITICO".equals(z.getPrediccion().getNivelAlerta()))
                .count();
        long rojas = zonas.stream()
                .filter(z -> z.getPrediccion() != null)
                .filter(z -> "ROJO".equals(z.getPrediccion().getNivelAlerta()))
                .count();
        long amarillas = zonas.stream()
                .filter(z -> z.getPrediccion() != null)
                .filter(z -> "AMARILLO".equals(z.getPrediccion().getNivelAlerta()))
                .count();
        long verdes = zonas.stream()
                .filter(z -> z.getPrediccion() != null)
                .filter(z -> "VERDE".equals(z.getPrediccion().getNivelAlerta()))
                .count();

        return String.format(
                "📊 Resumen de Alertas:\n" +
                        "   Total zonas: %d\n" +
                        "   🔴 CRITICO: %d\n" +
                        "   🟠 ROJO: %d\n" +
                        "   🟡 AMARILLO: %d\n" +
                        "   🟢 VERDE: %d",
                total, criticas, rojas, amarillas, verdes
        );
    }
}