// src/main/java/com/contaminacion/model/RegistroHistorico.java
package com.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroHistorico {

    @NotNull
    private LocalDateTime fecha;

    @NotNull @Min(0) @Max(500)
    private Double pm25;

    @NotNull @Min(0) @Max(300)
    private Double no2;

    @NotNull @Min(0) @Max(300)
    private Double so2;

    @NotNull @Min(0) @Max(3000)
    private Double co2;
}