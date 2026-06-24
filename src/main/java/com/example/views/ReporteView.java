// src/main/java/com/contaminacion/views/ReporteView.java
package com.example.views;

import com.example.model.Zona;
import com.example.service.ZonaService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Route(value = "reportes", layout = MainLayout.class)
@PageTitle("Exportar Reportes")
public class ReporteView extends VerticalLayout {

    private final ZonaService zonaService;

    public ReporteView(ZonaService zonaService) {
        this.zonaService = zonaService;
        setSizeFull();
        setPadding(true);

        H1 titulo = new H1("📄 Exportar Reporte de Datos");

        TextField txtNombreArchivo = new TextField("Nombre del archivo");
        txtNombreArchivo.setValue("reporte_" + LocalDateTime.now().toString().replace(":", "-").replace(".", "-") + ".txt");
        txtNombreArchivo.setWidth("400px");

        Button btnExportar = new Button("📥 Generar Reporte", e -> {
            try {
                generarReporte(txtNombreArchivo.getValue());
                Notification.show("✅ Reporte generado: " + txtNombreArchivo.getValue(),
                        5000, Notification.Position.MIDDLE);
            } catch (IOException ex) {
                Notification.show("❌ Error al generar reporte: " + ex.getMessage(),
                        5000, Notification.Position.MIDDLE);
            }
        });

        add(titulo, txtNombreArchivo, btnExportar);
    }

    private void generarReporte(String nombreArchivo) throws IOException {
        List<Zona> zonas = zonaService.listarTodas();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            writer.write("============================================================\n");
            writer.write("📊 REPORTE DE MONITOREO Y PREDICCIÓN DE CONTAMINACIÓN\n");
            writer.write("📅 Generado: " + LocalDateTime.now() + "\n");
            writer.write("============================================================\n\n");

            for (Zona zona : zonas) {
                writer.write("-----------------------------------------------------------------\n");
                writer.write("🏙️ ZONA: " + zona.getNombre() + "\n");
                writer.write("-----------------------------------------------------------------\n");

                writer.write("📊 MEDICIÓN ACTUAL:\n");
                writer.write(String.format("  - PM2.5: %.2f µg/m³\n",
                        zona.getContaminantesActuales().getPm25()));
                writer.write(String.format("  - NO2: %.2f µg/m³\n",
                        zona.getContaminantesActuales().getNo2()));
                writer.write(String.format("  - SO2: %.2f µg/m³\n",
                        zona.getContaminantesActuales().getSo2()));
                writer.write(String.format("  - CO2: %.2f ppm\n\n",
                        zona.getContaminantesActuales().getCo2()));

                writer.write("🌡️ FACTORES CLIMÁTICOS:\n");
                writer.write(String.format("  - Temperatura: %.1f °C\n",
                        zona.getFactoresClimaticos().getTemperatura()));
                writer.write(String.format("  - Velocidad Viento: %.1f km/h\n",
                        zona.getFactoresClimaticos().getVelocidadViento()));
                writer.write(String.format("  - Humedad: %.1f %%\n\n",
                        zona.getFactoresClimaticos().getHumedad()));

                if (zona.getPrediccion() != null) {
                    writer.write("🔮 PREDICCIÓN 24h:\n");
                    writer.write(String.format("  - PM2.5: %.2f µg/m³\n",
                            zona.getPrediccion().getPm25()));
                    writer.write(String.format("  - NO2: %.2f µg/m³\n",
                            zona.getPrediccion().getNo2()));
                    writer.write(String.format("  - SO2: %.2f µg/m³\n",
                            zona.getPrediccion().getSo2()));
                    writer.write("  - Alerta: " + zona.getPrediccion().getNivelAlerta() + "\n");
                    writer.write("  - Recomendaciones: \n");
                    for (String rec : zona.getPrediccion().getRecomendaciones()) {
                        writer.write("    * " + rec + "\n");
                    }
                    writer.write("\n");
                }

                // Histórico resumido
                if (zona.getHistorico30Dias() != null && !zona.getHistorico30Dias().isEmpty()) {
                    double promedioPM25 = zona.getHistorico30Dias().stream()
                            .mapToDouble(h -> h.getPm25())
                            .average().orElse(0.0);
                    writer.write("📈 PROMEDIO HISTÓRICO (30 días):\n");
                    writer.write(String.format("  - PM2.5: %.2f µg/m³\n", promedioPM25));
                    writer.write("  - Registros: " + zona.getHistorico30Dias().size() + " días\n\n");
                }
            }

            writer.write("============================================================\n");
            writer.write("FIN DEL REPORTE\n");
            writer.write("============================================================");
        }
    }
}