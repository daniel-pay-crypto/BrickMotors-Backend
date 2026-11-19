package com.brickmotors.ms_usuarios.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brickmotors.ms_usuarios.dto.ActualizarUsuarioRequest;
import com.brickmotors.ms_usuarios.dto.LoginRequest;
import com.brickmotors.ms_usuarios.dto.UsuarioRequest;
import com.brickmotors.ms_usuarios.dto.UsuarioResponse;
import com.brickmotors.ms_usuarios.exception.CredencialesInvalidasException;
import com.brickmotors.ms_usuarios.exception.DuplicateResourceException;
import com.brickmotors.ms_usuarios.exception.ReglaNegocioException;
import com.brickmotors.ms_usuarios.exception.ResourceNotFoundException;
import com.brickmotors.ms_usuarios.model.Rol;
import com.brickmotors.ms_usuarios.model.Usuario;
import com.brickmotors.ms_usuarios.repository.PedidoRepository;
import com.brickmotors.ms_usuarios.repository.UsuarioRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UsuarioService {

    private static final String MSG_CREDENCIALES = "Correo o contraseña incorrectos.";

    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PedidoRepository pedidoRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.pedidoRepository = pedidoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {
        return usuarioRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPorId(Long id) {
        return toResponse(buscarEntidad(id));
    }

    /** Registro público: siempre con rol CLIENTE. */
    @Transactional
    public UsuarioResponse registrar(UsuarioRequest request) {
        String correo = normalizar(request.correo());
        if (usuarioRepository.existsByCorreo(correo)) {
            throw new DuplicateResourceException("correo", "Ese correo ya está registrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre().trim());
        usuario.setCorreo(correo);
        usuario.setContrasena(passwordEncoder.encode(request.contrasena()));
        usuario.setActivo(true);
        usuario.setRol(Rol.CLIENTE);

        Usuario guardado = usuarioRepository.save(usuario);
        log.info("Usuario registrado id={}", guardado.getId());
        return toResponse(guardado);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(normalizar(request.correo()))
                .orElseThrow(() -> new CredencialesInvalidasException(MSG_CREDENCIALES));
        if (!Boolean.TRUE.equals(usuario.getActivo())
                || !passwordEncoder.matches(request.contrasena(), usuario.getContrasena())) {
            throw new CredencialesInvalidasException(MSG_CREDENCIALES);
        }
        return toResponse(usuario);
    }

    /** Edición desde el panel de administración (nombre, correo y tipo). */
    @Transactional
    public UsuarioResponse actualizar(Long id, ActualizarUsuarioRequest request) {
        Usuario usuario = buscarEntidad(id);
        String correo = normalizar(request.correo());
        if (usuarioRepository.existsByCorreoAndIdNot(correo, id)) {
            throw new DuplicateResourceException("correo", "Ese correo ya está registrado.");
        }
        usuario.setNombre(request.nombre().trim());
        usuario.setCorreo(correo);
        usuario.setRol(request.rol());
        log.info("Usuario actualizado id={} rol={}", id, request.rol());
        return toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = buscarEntidad(id);
        if (pedidoRepository.existsByUsuarioId(id)) {
            throw new ReglaNegocioException("No se puede eliminar un usuario que tiene pedidos.");
        }
        usuarioRepository.delete(usuario);
        log.info("Usuario eliminado id={}", id);
    }

    /** Usado también por PedidoService. */
    @Transactional(readOnly = true)
    public Usuario buscarEntidad(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un usuario con id " + id));
    }

    private String normalizar(String correo) {
        return correo.trim().toLowerCase();
    }

    private UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(
                u.getId(),
                u.getNombre(),
                u.getCorreo(),
                u.getRol().name(),
                u.getRol() == Rol.ADMIN,
                Boolean.TRUE.equals(u.getActivo()),
                u.getFechaRegistro());
    }
}