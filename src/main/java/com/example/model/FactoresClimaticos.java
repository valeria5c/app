// src/main/java/com/contaminacion/model/FactoresClimaticos.java
package com.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactoresClimaticos {

    @NotNull
    @Min(-40) @Max(60)
    private Double temperatura;

    @NotNull
    @Min(0) @Max(150)
    private Double velocidadViento;

    @NotNull
    @Min(0) @Max(100)
    private Double humedad;
}
