// src/main/java/com/contaminacion/model/Contaminantes.java
package com.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contaminantes {

    @NotNull
    @Min(0) @Max(500)
    private Double pm25;

    @NotNull
    @Min(0) @Max(300)
    private Double no2;

    @NotNull
    @Min(0) @Max(300)
    private Double so2;

    @NotNull
    @Min(0) @Max(3000)
    private Double co2;
}
