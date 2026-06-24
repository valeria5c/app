// src/main/java/com/contaminacion/model/Prediccion.java
package com.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prediccion {

    @NotNull
    private LocalDateTime fecha;

    @NotNull @Min(0) @Max(500)
    private Double pm25;

    @NotNull @Min(0) @Max(300)
    private Double no2;

    @NotNull @Min(0) @Max(300)
    private Double so2;

    @NotNull
    @Pattern(regexp = "^(VERDE|AMARILLO|ROJO|CRITICO)$",
            message = "Nivel de alerta inválido")
    private String nivelAlerta;

    @NotEmpty(message = "Debe incluir al menos una recomendación")
    private List<String> recomendaciones;
}
