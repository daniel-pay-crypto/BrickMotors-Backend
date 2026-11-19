package com.brickmotors.ms_usuarios.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private final Faker faker = new Faker();

    private Usuario usuarioExistente(Rol rol) {
        Usuario u = new Usuario();
        u.setId(1L);
        u.setNombre("Juan Pérez");
        u.setCorreo("juan.perez@gmail.com");
        u.setContrasena("hash-guardado");
        u.setActivo(true);
        u.setRol(rol);
        return u;
    }

    @Test
    void registrar_exitoso_encriptaContrasenaYAsignaCliente() {
        String correo = "cliente" + faker.number().digits(4) + "@gmail.com";
        UsuarioRequest request = new UsuarioRequest("Juan Pérez", correo, "Clave#123");

        when(usuarioRepository.existsByCorreo(correo)).thenReturn(false);
        when(passwordEncoder.encode("Clave#123")).thenReturn("hash-falso");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        UsuarioResponse r = usuarioService.registrar(request);

        assertEquals(1L, r.id());
        assertEquals("CLIENTE", r.rol());
        assertFalse(r.esAdmin());
        verify(passwordEncoder).encode("Clave#123");
    }

    @Test
    void registrar_correoDuplicado_noGuarda() {
        when(usuarioRepository.existsByCorreo("ana.soto@gmail.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> usuarioService.registrar(
                new UsuarioRequest("Ana Soto", "ana.soto@gmail.com", "Clave#123")));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void login_correcto_devuelveDatosDeSesion() {
        when(usuarioRepository.findByCorreo("juan.perez@gmail.com"))
                .thenReturn(Optional.of(usuarioExistente(Rol.ADMIN)));
        when(passwordEncoder.matches("Clave#123", "hash-guardado")).thenReturn(true);

        UsuarioResponse r = usuarioService.login(new LoginRequest("Juan.Perez@gmail.com", "Clave#123"));

        assertTrue(r.esAdmin());
        assertEquals("juan.perez@gmail.com", r.correo());
    }

    @Test
    void login_contrasenaIncorrecta_lanzaExcepcion() {
        when(usuarioRepository.findByCorreo("juan.perez@gmail.com"))
                .thenReturn(Optional.of(usuarioExistente(Rol.CLIENTE)));
        when(passwordEncoder.matches("mala", "hash-guardado")).thenReturn(false);

        assertThrows(CredencialesInvalidasException.class, () ->
                usuarioService.login(new LoginRequest("juan.perez@gmail.com", "mala")));
    }

    @Test
    void actualizar_cambiaElRol() {
        Usuario existente = usuarioExistente(Rol.CLIENTE);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.existsByCorreoAndIdNot("juan.perez@gmail.com", 1L)).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        UsuarioResponse r = usuarioService.actualizar(1L,
                new ActualizarUsuarioRequest("Juan Pérez", "juan.perez@gmail.com", Rol.VENDEDOR));

        assertEquals("VENDEDOR", r.rol());
    }

    @Test
    void eliminar_conPedidos_lanzaExcepcionYNoBorra() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente(Rol.CLIENTE)));
        when(pedidoRepository.existsByUsuarioId(1L)).thenReturn(true);

        assertThrows(ReglaNegocioException.class, () -> usuarioService.eliminar(1L));
        verify(usuarioRepository, never()).delete(any());
    }

    @Test
    void obtenerPorId_inexistente_lanzaNotFound() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.obtenerPorId(99L));
    }
}