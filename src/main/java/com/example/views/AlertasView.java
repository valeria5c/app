// src/main/java/com/contaminacion/views/AlertasView.java
package com.example.views;

import com.example.model.Zona;
import com.example.service.ZonaService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "alertas", layout = MainLayout.class)
@PageTitle("Alertas y Recomendaciones")
public class AlertasView extends VerticalLayout {

    private final ZonaService zonaService;
    private Grid<Zona> grid;

    public AlertasView(ZonaService zonaService) {
        this.zonaService = zonaService;
        setSizeFull();
        setPadding(true);

        H1 titulo = new H1("🚨 Alertas y Recomendaciones de Mitigación");
        Button btnRefrescar = new Button("🔄 Refrescar Alertas", e -> cargarDatos());

        configurarGrid();

        add(titulo, btnRefrescar, grid);
        cargarDatos();
    }

    private void configurarGrid() {
        grid = new Grid<>(Zona.class, false);
        grid.addColumn(Zona::getNombre).setHeader("Zona").setAutoWidth(true);
        grid.addColumn(z -> z.getContaminantesActuales().getPm25())
                .setHeader("PM2.5")
                .setWidth("100px");
        grid.addColumn(z -> z.getContaminantesActuales().getNo2())
                .setHeader("NO2")
                .setWidth("100px");

        grid.addComponentColumn(zona -> {
            String nivel = zona.getPrediccion() != null ?
                    zona.getPrediccion().getNivelAlerta() : "SIN DATOS";
            Span badge = new Span(nivel);
            badge.getElement().getThemeList().add("badge");
            switch (nivel) {
                case "CRITICO": badge.getElement().getThemeList().add("badge error"); break;
                case "ROJO": badge.getElement().getThemeList().add("badge error"); break;
                case "AMARILLO": badge.getElement().getThemeList().add("badge contrast"); break;
                case "VERDE": badge.getElement().getThemeList().add("badge success"); break;
            }
            return badge;
        }).setHeader("Alerta").setWidth("120px");

        grid.addColumn(zona -> {
            if (zona.getPrediccion() != null &&
                    zona.getPrediccion().getRecomendaciones() != null) {
                return String.join(" | ", zona.getPrediccion().getRecomendaciones());
            }
            return "Sin recomendaciones";
        }).setHeader("Recomendaciones").setAutoWidth(true);

        grid.setSizeFull();
    }

    private void cargarDatos() {
        try {
            List<Zona> zonas = zonaService.listarTodas();
            grid.setItems(zonas);
            Notification.show("✅ Alertas actualizadas", 3000,
                    Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("❌ Error: " + e.getMessage(), 5000,
                    Notification.Position.MIDDLE);
        }
    }
}
