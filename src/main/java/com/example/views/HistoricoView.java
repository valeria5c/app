// src/main/java/com/contaminacion/views/HistoricoView.java
package com.example.views;

import com.example.model.RegistroHistorico;
import com.example.model.Zona;
import com.example.service.ZonaService;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "historico", layout = MainLayout.class)
@PageTitle("Histórico vs OMS")
public class HistoricoView extends VerticalLayout {

    private final ZonaService zonaService;

    public HistoricoView(ZonaService zonaService) {
        this.zonaService = zonaService;
        setSizeFull();
        setPadding(true);

        H1 titulo = new H1("📈 Promedios Históricos vs Límites OMS");

        try {
            List<Zona> zonas = zonaService.listarTodas();
            for (Zona zona : zonas) {
                // Título de la zona
                Span zonaTitulo = new Span("🏙️ " + zona.getNombre());
                zonaTitulo.getStyle().set("font-size", "1.2rem");
                zonaTitulo.getStyle().set("font-weight", "bold");
                zonaTitulo.getStyle().set("margin-top", "1rem");
                add(zonaTitulo);

                List<RegistroHistorico> historico = zona.getHistorico30Dias();
                if (historico != null && !historico.isEmpty()) {
                    double promedioPM25 = historico.stream()
                            .mapToDouble(RegistroHistorico::getPm25)
                            .average().orElse(0.0);
                    double promedioNO2 = historico.stream()
                            .mapToDouble(RegistroHistorico::getNo2)
                            .average().orElse(0.0);
                    double promedioSO2 = historico.stream()
                            .mapToDouble(RegistroHistorico::getSo2)
                            .average().orElse(0.0);

                    add(crearLinea("PM2.5", promedioPM25, 15.0));
                    add(crearLinea("NO2", promedioNO2, 25.0));
                    add(crearLinea("SO2", promedioSO2, 40.0));
                } else {
                    add(new Paragraph("  Sin datos históricos disponibles"));
                }
                add(new Paragraph(""));
            }
        } catch (Exception e) {
            Notification.show("❌ Error al cargar datos: " + e.getMessage(), 5000,
                    Notification.Position.MIDDLE);
        }
    }

    private Paragraph crearLinea(String contaminante, double promedio, double limite) {
        String estado = promedio > limite ? "⚠️ FUERA DE NORMA" : "✅ DENTRO DE NORMA";
        String color = promedio > limite ? "#b00020" : "#1b8a3d";

        Paragraph p = new Paragraph(
                String.format("  - %s: %.2f µg/m³ (Límite OMS: %.1f) → ",
                        contaminante, promedio, limite) +
                        new Span(estado)
        );
        p.getStyle().set("color", color);
        return p;
    }
}