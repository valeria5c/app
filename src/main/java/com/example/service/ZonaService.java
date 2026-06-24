// src/main/java/com/contaminacion/service/ZonaService.java
package com.example.service;

import com.example.model.Zona;
import com.example.repository.ZonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ZonaService {

    private final ZonaRepository zonaRepository;

    public List<Zona> listarTodas() {
        return zonaRepository.findAll();
    }

    public Zona buscarPorId(String id) {
        return zonaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zona no encontrada"));
    }

    public Zona buscarPorNombre(String nombre) {
        return zonaRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Zona no encontrada"));
    }

    public Zona guardar(Zona zona) {
        zona.setUltimaActualizacion(LocalDateTime.now());
        return zonaRepository.save(zona);
    }

    public void eliminar(String id) {
        zonaRepository.deleteById(id);
    }

    public List<Zona> buscarZonasCriticas() {
        return zonaRepository.findByPrediccionNivelAlerta("CRITICO");
    }

    public List<Zona> buscarZonasConAltaContaminacion() {
        return zonaRepository.findByContaminantesActualesPm25GreaterThan(25.0);
    }
}
