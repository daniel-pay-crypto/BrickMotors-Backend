package com.brickmotors.ms_usuarios.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brickmotors.ms_usuarios.dto.MensajeContactoRequest;
import com.brickmotors.ms_usuarios.dto.MensajeContactoResponse;
import com.brickmotors.ms_usuarios.service.ContactoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/contacto")
@Tag(name = "Contacto", description = "Mensajes del formulario de contacto")
public class ContactoController {

    private final ContactoService contactoService;

    public ContactoController(ContactoService contactoService) {
        this.contactoService = contactoService;
    }

    @PostMapping
    @Operation(summary = "Enviar un mensaje de contacto")
    public ResponseEntity<MensajeContactoResponse> enviar(@Valid @RequestBody MensajeContactoRequest request) {
        MensajeContactoResponse creado = contactoService.enviar(request);
        return ResponseEntity.created(URI.create("/api/v1/contacto/" + creado.id())).body(creado);
    }

    @GetMapping
    @Operation(summary = "Listar mensajes (panel de administración)")
    public ResponseEntity<List<MensajeContactoResponse>> listar() {
        return ResponseEntity.ok(contactoService.listar());
    }

    @PatchMapping("/{id}/leido")
    @Operation(summary = "Marcar un mensaje como leído")
    public ResponseEntity<MensajeContactoResponse> marcarLeido(@PathVariable("id") Long id) {
        return ResponseEntity.ok(contactoService.marcarLeido(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un mensaje")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        contactoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}