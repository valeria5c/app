// src/main/java/com/contaminacion/views/PrediccionView.java
package com.example.views;

import com.example.model.Zona;
import com.example.service.PrediccionService;
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

@Route(value = "prediccion", layout = MainLayout.class)
@PageTitle("Predicción 24h")
public class PrediccionView extends VerticalLayout {

    private final ZonaService zonaService;
    private final PrediccionService prediccionService;
    private Grid<Zona> grid;

    public PrediccionView(ZonaService zonaService, PrediccionService prediccionService) {
        this.zonaService = zonaService;
        this.prediccionService = prediccionService;
        setSizeFull();
        setPadding(true);

        H1 titulo = new H1("🔮 Predicción de Contaminación (Próximas 24 horas)");
        Button btnPredecir = new Button("🔮 Generar Predicción", e -> generarPrediccion());

        configurarGrid();

        add(titulo, btnPredecir, grid);
        cargarDatos();
    }

    private void configurarGrid() {
        grid = new Grid<>(Zona.class, false);
        grid.addColumn(Zona::getNombre).setHeader("Zona").setAutoWidth(true);
        grid.addColumn(z -> z.getPrediccion() != null ?
                        z.getPrediccion().getPm25() : 0.0)
                .setHeader("PM2.5 Estimado")
                .setWidth("150px");
        grid.addColumn(z -> z.getPrediccion() != null ?
                        z.getPrediccion().getNo2() : 0.0)
                .setHeader("NO2 Estimado")
                .setWidth("150px");
        grid.addColumn(z -> z.getPrediccion() != null ?
                        z.getPrediccion().getSo2() : 0.0)
                .setHeader("SO2 Estimado")
                .setWidth("150px");

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

        grid.setSizeFull();
    }

    private void generarPrediccion() {
        try {
            List<Zona> zonas = zonaService.listarTodas();
            for (Zona zona : zonas) {
                var prediccion = prediccionService.calcularPrediccion(zona);
                zona.setPrediccion(prediccion);
                zonaService.guardar(zona);
            }
            cargarDatos();
            Notification.show("✅ Predicciones generadas y guardadas en MongoDB",
                    3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("❌ Error: " + e.getMessage(), 5000,
                    Notification.Position.MIDDLE);
        }
    }

    private void cargarDatos() {
        grid.setItems(zonaService.listarTodas());
    }
}