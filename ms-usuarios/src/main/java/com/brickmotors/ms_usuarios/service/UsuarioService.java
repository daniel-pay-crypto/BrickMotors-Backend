package com.brickmotors.ms_usuarios.service;

import com.brickmotors.ms_usuarios.dto.UsuarioRequest;
import com.brickmotors.ms_usuarios.dto.UsuarioResponse;
import com.brickmotors.ms_usuarios.model.Usuario;
import com.brickmotors.ms_usuarios.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional 
public class UsuarioService {


    private static final String MSG_CREDENCIALES = "Correo o contraseña incorrectos.";

    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;
    private final PasswordEncoder passwordEncoder;

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
