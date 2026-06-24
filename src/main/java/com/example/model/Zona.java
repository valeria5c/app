// src/main/java/com/contaminacion/model/Zona.java
package com.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "zonas")
public class Zona {

    @Id
    private String id;

    @NotNull(message = "El nombre de la zona es obligatorio")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Indexed(unique = true)
    private String nombre;

    @NotNull
    @Valid
    @Field("contaminantesActuales")
    private Contaminantes contaminantesActuales;

    @NotNull
    @Valid
    @Field("factoresClimaticos")
    private FactoresClimaticos factoresClimaticos;

    @NotNull
    @Size(max = 30, message = "El histórico no puede superar los 30 registros")
    private List<@Valid RegistroHistorico> historico30Dias;

    @NotNull
    @Valid
    private Prediccion prediccion;

    @Field("ultimaActualizacion")
    private LocalDateTime ultimaActualizacion;
}