package com.brickmotors.ms_usuarios.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.brickmotors.ms_usuarios.dto.CategoriaRequest;
import com.brickmotors.ms_usuarios.dto.CategoriaResponse;
import com.brickmotors.ms_usuarios.exception.DuplicateResourceException;
import com.brickmotors.ms_usuarios.exception.ReglaNegocioException;
import com.brickmotors.ms_usuarios.exception.ResourceNotFoundException;
import com.brickmotors.ms_usuarios.model.Categoria;
import com.brickmotors.ms_usuarios.repository.CategoriaRepository;
import com.brickmotors.ms_usuarios.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void listar_devuelveTodasLasCategorias() {
        when(categoriaRepository.findAll(any(Sort.class))).thenReturn(List.of(
                new Categoria(1L, "MOC"),
                new Categoria(2L, "Star Wars")));

        List<CategoriaResponse> resultado = categoriaService.listar();

        assertEquals(2, resultado.size());
        assertEquals("MOC", resultado.get(0).nombre());
    }

    @Test
    void crear_exitoso_guardaYDevuelveConId() {
        when(categoriaRepository.existsByNombreIgnoreCase("Ninjago")).thenReturn(false);
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(inv -> {
            Categoria c = inv.getArgument(0);
            c.setId(10L);
            return c;
        });

        CategoriaResponse r = categoriaService.crear(new CategoriaRequest("  Ninjago  "));

        assertEquals(10L, r.id());
        assertEquals("Ninjago", r.nombre()); // se guarda sin espacios
    }

    @Test
    void crear_nombreDuplicado_lanzaExcepcionYNoGuarda() {
        when(categoriaRepository.existsByNombreIgnoreCase("Technic")).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> categoriaService.crear(new CategoriaRequest("Technic")));
        verify(categoriaRepository, never()).save(any());
    }

    @Test
    void actualizar_cambiaElNombre() {
        Categoria existente = new Categoria(1L, "Tecnic");
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(categoriaRepository.existsByNombreIgnoreCaseAndIdNot("Technic", 1L)).thenReturn(false);
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(inv -> inv.getArgument(0));

        CategoriaResponse r = categoriaService.actualizar(1L, new CategoriaRequest("Technic"));

        assertEquals("Technic", r.nombre());
    }

    @Test
    void eliminar_conProductos_lanzaExcepcionYNoBorra() {
        when(categoriaRepository.findById(2L)).thenReturn(Optional.of(new Categoria(2L, "Star Wars")));
        when(productoRepository.existsByCategoriaId(2L)).thenReturn(true);

        assertThrows(ReglaNegocioException.class, () -> categoriaService.eliminar(2L));
        verify(categoriaRepository, never()).delete(any());
    }

    @Test
    void eliminar_sinProductos_borra() {
        Categoria vacia = new Categoria(3L, "MOC");
        when(categoriaRepository.findById(3L)).thenReturn(Optional.of(vacia));
        when(productoRepository.existsByCategoriaId(3L)).thenReturn(false);

        categoriaService.eliminar(3L);

        verify(categoriaRepository).delete(vacia);
    }

    @Test
    void obtenerPorId_inexistente_lanzaNotFound() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoriaService.obtenerPorId(99L));
    }
}