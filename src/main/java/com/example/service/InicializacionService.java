// src/main/java/com/contaminacion/service/InicializacionService.java
package com.example.service;

import com.example.model.*;
import com.example.repository.ZonaRepository;
import com.example.service.PrediccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class InicializacionService implements CommandLineRunner {

    private final ZonaRepository zonaRepository;
    private final PrediccionService prediccionService;

    @Override
    public void run(String... args) throws Exception {
        if (zonaRepository.count() == 0) {
            System.out.println("📦 Inicializando base de datos con 5 zonas de prueba...");
            crearZonasDePrueba();
            System.out.println("✅ Datos de prueba cargados exitosamente.");
            System.out.println("🌐 Accede a la aplicación en: http://localhost:8080");
        }
    }

    private void crearZonasDePrueba() {
        String[] nombres = {"Centro Histórico", "Zona Industrial Norte",
                "Valle de los Chillos", "La Floresta", "Condado"};
        Random random = new Random();

        for (String nombre : nombres) {
            Zona zona = new Zona();
            zona.setNombre(nombre);
            zona.setUltimaActualizacion(LocalDateTime.now());

            // Contaminantes actuales
            double pm25, no2, so2, co2;
            if (nombre.equals("Zona Industrial Norte")) {
                pm25 = 45.0 + random.nextDouble() * 30.0;
                no2 = 50.0 + random.nextDouble() * 25.0;
                so2 = 35.0 + random.nextDouble() * 20.0;
                co2 = 800.0 + random.nextDouble() * 500.0;
            } else if (nombre.equals("Centro Histórico")) {
                pm25 = 25.0 + random.nextDouble() * 15.0;
                no2 = 30.0 + random.nextDouble() * 15.0;
                so2 = 20.0 + random.nextDouble() * 10.0;
                co2 = 500.0 + random.nextDouble() * 300.0;
            } else if (nombre.equals("Valle de los Chillos")) {
                pm25 = 8.0 + random.nextDouble() * 8.0;
                no2 = 12.0 + random.nextDouble() * 8.0;
                so2 = 8.0 + random.nextDouble() * 6.0;
                co2 = 390.0 + random.nextDouble() * 100.0;
            } else {
                pm25 = 10.0 + random.nextDouble() * 12.0;
                no2 = 15.0 + random.nextDouble() * 10.0;
                so2 = 10.0 + random.nextDouble() * 8.0;
                co2 = 400.0 + random.nextDouble() * 150.0;
            }

            Contaminantes contaminantes = new Contaminantes(pm25, no2, so2, co2);
            zona.setContaminantesActuales(contaminantes);

            // Factores climáticos
            FactoresClimaticos clima = new FactoresClimaticos(
                    12.0 + random.nextDouble() * 15.0,
                    2.0 + random.nextDouble() * 25.0,
                    40.0 + random.nextDouble() * 55.0
            );
            zona.setFactoresClimaticos(clima);

            // Histórico de 30 días
            List<RegistroHistorico> historico = new ArrayList<>();
            for (int i = 29; i >= 0; i--) {
                RegistroHistorico reg = new RegistroHistorico();
                reg.setFecha(LocalDateTime.now().minusDays(i));
                reg.setPm25(contaminantes.getPm25() * (0.8 + random.nextDouble() * 0.4));
                reg.setNo2(contaminantes.getNo2() * (0.8 + random.nextDouble() * 0.4));
                reg.setSo2(contaminantes.getSo2() * (0.8 + random.nextDouble() * 0.4));
                reg.setCo2(contaminantes.getCo2() * (0.9 + random.nextDouble() * 0.2));
                historico.add(reg);
            }
            zona.setHistorico30Dias(historico);

            // Calcular predicción inicial
            Prediccion prediccion = prediccionService.calcularPrediccion(zona);
            zona.setPrediccion(prediccion);

            zonaRepository.save(zona);
        }
    }
}