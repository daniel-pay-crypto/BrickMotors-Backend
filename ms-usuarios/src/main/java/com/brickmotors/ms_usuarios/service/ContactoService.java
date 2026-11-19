package com.brickmotors.ms_usuarios.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brickmotors.ms_usuarios.dto.MensajeContactoRequest;
import com.brickmotors.ms_usuarios.dto.MensajeContactoResponse;
import com.brickmotors.ms_usuarios.exception.ResourceNotFoundException;
import com.brickmotors.ms_usuarios.model.MensajeContacto;
import com.brickmotors.ms_usuarios.repository.MensajeContactoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ContactoService {

    private final MensajeContactoRepository repository;

    public ContactoService(MensajeContactoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public MensajeContactoResponse enviar(MensajeContactoRequest request) {
        MensajeContacto m = new MensajeContacto();
        m.setNombre(request.nombre().trim());
        m.setCorreo(request.correo().trim().toLowerCase());
        m.setMensaje(request.mensaje().trim());
        MensajeContacto guardado = repository.save(m);
        log.info("Mensaje de contacto recibido id={}", guardado.getId());
        return toResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<MensajeContactoResponse> listar() {
        return repository.findAllByOrderByFechaDesc().stream().map(this::toResponse).toList();
    }

    @Transactional
    public MensajeContactoResponse marcarLeido(Long id) {
        MensajeContacto m = buscar(id);
        m.setLeido(true);
        return toResponse(repository.save(m));
    }

    @Transactional
    public void eliminar(Long id) {
        repository.delete(buscar(id));
    }

    private MensajeContacto buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un mensaje con id " + id));
    }

    private MensajeContactoResponse toResponse(MensajeContacto m) {
        return new MensajeContactoResponse(m.getId(), m.getNombre(), m.getCorreo(),
                m.getMensaje(), m.getFecha(), Boolean.TRUE.equals(m.getLeido()));
    }
}