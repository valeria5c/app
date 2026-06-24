// src/main/java/com/contaminacion/service/PrediccionService.java
package com.example.service;

import com.example.model.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrediccionService {

    private static final double LIMITE_PM25 = 15.0;
    private static final double LIMITE_NO2 = 25.0;
    private static final double LIMITE_SO2 = 40.0;

    public Prediccion calcularPrediccion(Zona zona) {
        Contaminantes actuales = zona.getContaminantesActuales();
        FactoresClimaticos clima = zona.getFactoresClimaticos();
        List<RegistroHistorico> historico = zona.getHistorico30Dias();

        double promHistoricoPM25 = calcularPromedioReciente(historico, "pm25");
        double factorClimatico = calcularFactorClimatico(clima);

        double basePM25 = (actuales.getPm25() * 0.7) + (promHistoricoPM25 * 0.3);
        double predPM25 = Math.max(2.0, basePM25 * factorClimatico);
        double predSO2 = Math.max(1.0, actuales.getSo2() * factorClimatico);
        double predNO2 = Math.max(1.0, actuales.getNo2() * factorClimatico);

        String nivelAlerta = determinarNivelAlerta(predPM25, predNO2, predSO2);
        List<String> recomendaciones = generarRecomendaciones(nivelAlerta);

        return Prediccion.builder()
                .fecha(LocalDateTime.now().plusHours(24))
                .pm25(predPM25)
                .no2(predNO2)
                .so2(predSO2)
                .nivelAlerta(nivelAlerta)
                .recomendaciones(recomendaciones)
                .build();
    }

    private double calcularFactorClimatico(FactoresClimaticos clima) {
        double factor = 1.0;
        double viento = clima.getVelocidadViento();
        double temp = clima.getTemperatura();
        double humedad = clima.getHumedad();

        if (viento < 5.0) factor += 0.35;
        else if (viento < 15.0) factor += 0.15;
        else if (viento > 25.0) factor -= 0.25;

        if (temp > 30.0) factor += 0.20;
        else if (temp < 10.0) factor += 0.10;

        if (humedad > 85.0) factor -= 0.10;
        else if (humedad < 30.0) factor += 0.05;

        return factor;
    }

    private double calcularPromedioReciente(List<RegistroHistorico> historico, String contaminante) {
        if (historico == null || historico.isEmpty()) return 15.0;
        int inicio = Math.max(0, historico.size() - 3);
        double suma = 0;
        for (int i = inicio; i < historico.size(); i++) {
            RegistroHistorico reg = historico.get(i);
            if ("pm25".equals(contaminante)) suma += reg.getPm25();
            else if ("no2".equals(contaminante)) suma += reg.getNo2();
            else if ("so2".equals(contaminante)) suma += reg.getSo2();
        }
        return suma / (historico.size() - inicio);
    }

    private String determinarNivelAlerta(double pm25, double no2, double so2) {
        if (pm25 > LIMITE_PM25 * 3 || no2 > LIMITE_NO2 * 3 || so2 > LIMITE_SO2 * 3) {
            return "CRITICO";
        } else if (pm25 > LIMITE_PM25 * 2 || no2 > LIMITE_NO2 * 2 || so2 > LIMITE_SO2 * 2) {
            return "ROJO";
        } else if (pm25 > LIMITE_PM25 || no2 > LIMITE_NO2 || so2 > LIMITE_SO2) {
            return "AMARILLO";
        } else {
            return "VERDE";
        }
    }

    private List<String> generarRecomendaciones(String nivelAlerta) {
        List<String> recomendaciones = new ArrayList<>();
        switch (nivelAlerta) {
            case "CRITICO":
                recomendaciones.add("🚨 PARALIZACIÓN TOTAL: Suspender actividades industriales y escolares");
                recomendaciones.add("🚫 Restricción vehicular TOTAL en toda la zona");
                recomendaciones.add("😷 Uso OBLIGATORIO de mascarillas N95");
                recomendaciones.add("🏠 Recomendar teletrabajo para toda la población");
                break;
            case "ROJO":
                recomendaciones.add("⚠️ Suspender actividades al aire libre");
                recomendaciones.add("🚗 Restringir circulación vehicular (pico y placa)");
                recomendaciones.add("🏭 Cierre temporal de industrias cercanas");
                recomendaciones.add("😷 Uso de mascarillas para grupos vulnerables");
                break;
            case "AMARILLO":
                recomendaciones.add("📢 Recomendar uso de transporte público");
                recomendaciones.add("🏃 Grupos de riesgo evitar esfuerzos prolongados al aire libre");
                recomendaciones.add("🌳 Aumentar áreas verdes para mitigación");
                break;
            case "VERDE":
                recomendaciones.add("✅ Calidad de aire óptima. No se requieren restricciones");
                recomendaciones.add("🌿 Mantener monitoreo periódico");
                break;
        }
        return recomendaciones;
    }
}