// src/main/java/com/contaminacion/repository/ZonaRepository.java
package com.example.repository;

import com.contaminacion.model.Zona;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZonaRepository extends MongoRepository<Zona, String> {

    Optional<Zona> findByNombre(String nombre);

    List<Zona> findByContaminantesActualesPm25GreaterThan(double limite);

    List<Zona> findByPrediccionNivelAlerta(String nivelAlerta);

    List<Zona> findTop5ByOrderByUltimaActualizacionDesc();

    @Query("{ 'prediccion.nivelAlerta': ?0, 'contaminantesActuales.co2': { $gt: ?1 } }")
    List<Zona> findZonasCriticasPorGases(String nivelAlerta, double co2Limite);
}