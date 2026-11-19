package com.brickmotors.ms_usuarios.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.brickmotors.ms_usuarios.dto.MensajeContactoRequest;
import com.brickmotors.ms_usuarios.dto.MensajeContactoResponse;
import com.brickmotors.ms_usuarios.exception.ResourceNotFoundException;
import com.brickmotors.ms_usuarios.model.MensajeContacto;
import com.brickmotors.ms_usuarios.repository.MensajeContactoRepository;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class ContactoServiceTest {

    @Mock
    private MensajeContactoRepository repository;

    @InjectMocks
    private ContactoService contactoService;

    private final Faker faker = new Faker();

    private MensajeContacto mensajeGuardado(Long id, boolean leido) {
        MensajeContacto m = new MensajeContacto();
        m.setId(id);
        m.setNombre("Juan Pérez");
        m.setCorreo("juan.perez@gmail.com");
        m.setMensaje("¿Tienen stock del Halcón Milenario?");
        m.setFecha(LocalDateTime.now());
        m.setLeido(leido);
        return m;
    }

    @Test
    void enviar_normalizaDatosYGuarda() {
        String texto = faker.lorem().sentence();
        when(repository.save(any(MensajeContacto.class))).thenAnswer(inv -> {
            MensajeContacto m = inv.getArgument(0);
            m.setId(1L);
            return m;
        });

        MensajeContactoResponse r = contactoService.enviar(
                new MensajeContactoRequest("  Juan Pérez ", " Juan.Perez@GMAIL.com ", "  " + texto + "  "));

        ArgumentCaptor<MensajeContacto> captor = ArgumentCaptor.forClass(MensajeContacto.class);
        verify(repository).save(captor.capture());
        MensajeContacto guardado = captor.getValue();

        assertEquals(1L, r.id());
        assertEquals("Juan Pérez", guardado.getNombre());
        assertEquals("juan.perez@gmail.com", guardado.getCorreo());
        assertEquals(texto, guardado.getMensaje());
        assertFalse(r.leido());
    }

    @Test
    void listar_devuelveMensajesOrdenados() {
        when(repository.findAllByOrderByFechaDesc())
                .thenReturn(List.of(mensajeGuardado(2L, false), mensajeGuardado(1L, true)));

        List<MensajeContactoResponse> resultado = contactoService.listar();

        assertEquals(2, resultado.size());
        assertEquals(2L, resultado.get(0).id());
    }

    @Test
    void marcarLeido_cambiaElEstado() {
        when(repository.findById(1L)).thenReturn(Optional.of(mensajeGuardado(1L, false)));
        when(repository.save(any(MensajeContacto.class))).thenAnswer(inv -> inv.getArgument(0));

        MensajeContactoResponse r = contactoService.marcarLeido(1L);

        assertTrue(r.leido());
    }

    @Test
    void eliminar_existente_borra() {
        MensajeContacto m = mensajeGuardado(1L, false);
        when(repository.findById(1L)).thenReturn(Optional.of(m));

        contactoService.eliminar(1L);

        verify(repository).delete(m);
    }

    @Test
    void eliminar_inexistente_lanzaNotFoundYNoBorra() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> contactoService.eliminar(99L));
        verify(repository, never()).delete(any());
    }
}