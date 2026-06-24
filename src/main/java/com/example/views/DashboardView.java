// src/main/java/com/contaminacion/views/DashboardView.java
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

@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard - Monitoreo")
public class DashboardView extends VerticalLayout {

    private final ZonaService zonaService;
    private Grid<Zona> grid;

    public DashboardView(ZonaService zonaService) {
        this.zonaService = zonaService;
        setSizeFull();
        setPadding(true);

        H1 titulo = new H1("📊 Monitoreo de Contaminación Actual");
        titulo.getStyle().set("margin-bottom", "1rem");

        Button btnActualizar = new Button("🔄 Actualizar desde MongoDB", e -> actualizarGrid());

        configurarGrid();

        add(titulo, btnActualizar, grid);
        actualizarGrid();
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
        grid.addColumn(z -> z.getContaminantesActuales().getSo2())
                .setHeader("SO2")
                .setWidth("100px");
        grid.addColumn(z -> z.getContaminantesActuales().getCo2())
                .setHeader("CO2")
                .setWidth("100px");

        grid.addComponentColumn(this::crearBadgeAlerta)
                .setHeader("Alerta")
                .setWidth("120px");

        grid.addColumn(z -> z.getUltimaActualizacion() != null ?
                        z.getUltimaActualizacion().toString().substring(0, 16) : "N/A")
                .setHeader("Última Actualización");

        grid.setSizeFull();
    }

    private Span crearBadgeAlerta(Zona zona) {
        String nivel = zona.getPrediccion() != null ?
                zona.getPrediccion().getNivelAlerta() : "SIN DATOS";
        Span badge = new Span(nivel);
        badge.getElement().getThemeList().add("badge");
        badge.getStyle().set("border-radius", "12px");
        badge.getStyle().set("padding", "0.25rem 0.75rem");
        badge.getStyle().set("font-size", "0.875rem");

        switch (nivel) {
            case "CRITICO":
                badge.getStyle().set("background", "#b00020");
                badge.getStyle().set("color", "white");
                break;
            case "ROJO":
                badge.getElement().getThemeList().add("badge error");
                break;
            case "AMARILLO":
                badge.getElement().getThemeList().add("badge contrast");
                break;
            case "VERDE":
                badge.getElement().getThemeList().add("badge success");
                break;
            default:
                badge.getElement().getThemeList().add("badge contrast");
        }
        return badge;
    }

    private void actualizarGrid() {
        try {
            List<Zona> zonas = zonaService.listarTodas();
            grid.setItems(zonas);
            Notification.show("✅ Datos actualizados desde MongoDB", 3000,
                    Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("❌ Error al cargar datos: " + e.getMessage(),
                    5000, Notification.Position.MIDDLE);
        }
    }
}