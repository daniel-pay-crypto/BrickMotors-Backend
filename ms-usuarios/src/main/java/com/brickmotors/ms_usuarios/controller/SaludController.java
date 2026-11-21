package com.brickmotors.ms_usuarios.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * GET /api/health -> 200 {"estado":"ok"}
 * La usan Render (Health Check Path) y UptimeRobot (cada 5 minutos).
 * A propósito NO consulta la base de datos, para no despertar a Neon cada 5 minutos.
 */
@RestController
@Tag(name = "Salud", description = "Estado del servicio")
public class SaludController {

    @GetMapping("/api/health")
    @Operation(summary = "Comprueba que la API está viva")
    public Map<String, String> salud() {
        return Map.of("estado", "ok");
    }
}