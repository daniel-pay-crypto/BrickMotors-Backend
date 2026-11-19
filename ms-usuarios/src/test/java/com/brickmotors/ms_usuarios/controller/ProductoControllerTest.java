package com.brickmotors.ms_usuarios.controller;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.brickmotors.ms_usuarios.dto.ProductoResponse;
import com.brickmotors.ms_usuarios.exception.GlobalExceptionHandler;
import com.brickmotors.ms_usuarios.exception.ResourceNotFoundException;
import com.brickmotors.ms_usuarios.service.ProductoService;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void listar_devuelve200() throws Exception {
        ProductoResponse atst = new ProductoResponse(1L, "ATST-001", "Caminante AT-ST", null,
                2L, "Star Wars", 150000, 5, 0, false, 150000, "/img/at-st.png");
        when(productoService.listar(null)).thenReturn(List.of(atst));

        mockMvc.perform(get("/api/v1/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo").value("ATST-001"))
                .andExpect(jsonPath("$[0].categoria").value("Star Wars"));
    }

    @Test
    void crear_invalido_devuelve400ConErroresPorCampo() throws Exception {
        String json = """
                {"codigo": "", "nombre": "Set", "categoriaId": 1, "precio": 0, "stock": 1}
                """;

        mockMvc.perform(post("/api/v1/productos").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.codigo").value("El código no puede estar vacío."))
                .andExpect(jsonPath("$.errores.precio").value("El precio debe ser mayor a 0."));

        verifyNoInteractions(productoService);
    }

    @Test
    void obtener_inexistente_devuelve404() throws Exception {
        when(productoService.obtenerPorId(99L))
                .thenThrow(new ResourceNotFoundException("No existe un producto con id 99"));

        mockMvc.perform(get("/api/v1/productos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detalle").value("No existe un producto con id 99"));
    }
}