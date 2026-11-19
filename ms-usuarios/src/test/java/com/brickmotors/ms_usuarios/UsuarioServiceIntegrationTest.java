package com.brickmotors.ms_usuarios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.brickmotors.ms_usuarios.dto.LoginRequest;
import com.brickmotors.ms_usuarios.dto.UsuarioRequest;
import com.brickmotors.ms_usuarios.dto.UsuarioResponse;
import com.brickmotors.ms_usuarios.exception.CredencialesInvalidasException;
import com.brickmotors.ms_usuarios.exception.DuplicateResourceException;
import com.brickmotors.ms_usuarios.model.Usuario;
import com.brickmotors.ms_usuarios.repository.UsuarioRepository;
import com.brickmotors.ms_usuarios.service.UsuarioService;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UsuarioServiceIntegrationTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void registrar_guardaClienteConContrasenaEncriptada() {
        UsuarioResponse r = usuarioService.registrar(
                new UsuarioRequest("Juan Pérez", "Juan.Perez@gmail.com", "Clave#123"));

        assertNotNull(r.id());
        assertEquals("juan.perez@gmail.com", r.correo());
        assertEquals("CLIENTE", r.rol());
        assertFalse(r.esAdmin());

        Usuario enBd = usuarioRepository.findById(r.id()).orElseThrow();
        assertTrue(enBd.getContrasena().startsWith("$2a$"));
    }

    @Test
    void registrar_correoRepetido_lanzaDuplicate() {
        usuarioService.registrar(new UsuarioRequest("Ana Soto", "ana.soto@gmail.com", "Clave#123"));

        assertThrows(DuplicateResourceException.class, () ->
                usuarioService.registrar(new UsuarioRequest("Ana Soto", "ana.soto@gmail.com", "Otra#4567")));
    }

    @Test
    void login_adminInicial_esAdmin() {
        UsuarioResponse admin = usuarioService.login(new LoginRequest("admin@gmail.com", "Admin123"));
        assertTrue(admin.esAdmin());
    }

    @Test
    void login_contrasenaIncorrecta_lanza401() {
        assertThrows(CredencialesInvalidasException.class, () ->
                usuarioService.login(new LoginRequest("admin@gmail.com", "incorrecta")));
    }
}