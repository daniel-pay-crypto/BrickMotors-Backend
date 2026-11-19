package com.brickmotors.ms_usuarios;

import com.brickmotors.ms_usuarios.dto.UsuarioRequest;
import com.brickmotors.ms_usuarios.dto.UsuarioResponse;
import com.brickmotors.ms_usuarios.repository.UsuarioRepository;
import com.brickmotors.ms_usuarios.service.UsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import org.springframework.security.crypto.password.PasswordEncoder;

import com.brickmotors.ms_usuarios.exception.DuplicateResourceException;
import com.brickmotors.ms_usuarios.exception.ResourceNotFoundException;
import com.brickmotors.ms_usuarios.model.Usuario;
import com.brickmotors.ms_usuarios.repository.PedidoRepository;

import net.datafaker.Faker;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService1;

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private final Faker faker = new Faker();

    @Test
    void registrar_exitoso_encriptaYAsignaCliente() {
        String correo = "cliente" + faker.number().digits(4) + "@gmail.com";
        UsuarioRequest request = new UsuarioRequest("Juan Pérez", correo, "Clave#123");

        when(usuarioRepository.existsByCorreo(correo)).thenReturn(false);
        when(passwordEncoder.encode("Clave#123")).thenReturn("hash-falso");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        UsuarioResponse r = usuarioService1.registrar(request);

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
    void obtenerPorId_inexistente_lanzaNotFound() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.obtenerPorId(99L));
    }
}



