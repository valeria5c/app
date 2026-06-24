// src/main/java/com/contaminacion/Application.java
package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.example.repository")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("============================================================");
        System.out.println("🌍 SISTEMA DE GESTIÓN Y PREDICCIÓN DE CONTAMINACIÓN");
        System.out.println("============================================================");
        System.out.println("✅ Aplicación iniciada en: http://localhost:8080");
        System.out.println("🔗 Conectado a MongoDB local: mongodb://localhost:27017");
        System.out.println("============================================================");
    }
}
