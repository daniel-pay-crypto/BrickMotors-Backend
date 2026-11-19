package com.brickmotors.ms_usuarios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.brickmotors.ms_usuarios.dto.ItemPedidoRequest;
import com.brickmotors.ms_usuarios.dto.PedidoRequest;
import com.brickmotors.ms_usuarios.dto.PedidoResponse;
import com.brickmotors.ms_usuarios.exception.ReglaNegocioException;
import com.brickmotors.ms_usuarios.model.EstadoPedido;
import com.brickmotors.ms_usuarios.model.Producto;
import com.brickmotors.ms_usuarios.repository.ProductoRepository;
import com.brickmotors.ms_usuarios.repository.UsuarioRepository;
import com.brickmotors.ms_usuarios.service.PedidoService;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PedidoServiceIntegrationTest {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Long adminId() {
        return usuarioRepository.findByCorreo("admin@gmail.com").orElseThrow().getId();
    }

    private Producto atst() {
        // Caminante AT-ST: precio 150000, stock 5, sin descuento (V2)
        return productoRepository.findByCodigoIgnoreCase("ATST-001").orElseThrow();
    }

    @Test
    void crear_descuentaStockYCalculaTotal() {
        Producto p = atst();

        PedidoResponse pedido = pedidoService.crear(
                new PedidoRequest(adminId(), List.of(new ItemPedidoRequest(p.getId(), 2))));

        assertEquals(300000, pedido.total());
        assertEquals(EstadoPedido.PENDIENTE, pedido.estado());
        assertEquals(3, atst().getStock());
    }

    @Test
    void crear_conDescuento_aplicaPrecioFinal() {
        // SW-008: 49990 con 20% → 39992
        Producto sw008 = productoRepository.findByCodigoIgnoreCase("SW-008").orElseThrow();

        PedidoResponse pedido = pedidoService.crear(
                new PedidoRequest(adminId(), List.of(new ItemPedidoRequest(sw008.getId(), 1))));

        assertEquals(39992, pedido.total());
    }

    @Test
    void crear_sinStockSuficiente_lanzaExcepcion() {
        Producto p = atst();

        assertThrows(ReglaNegocioException.class, () -> pedidoService.crear(
                new PedidoRequest(adminId(), List.of(new ItemPedidoRequest(p.getId(), 99)))));
    }

    @Test
    void cancelar_devuelveElStock() {
        Producto p = atst();
        PedidoResponse pedido = pedidoService.crear(
                new PedidoRequest(adminId(), List.of(new ItemPedidoRequest(p.getId(), 2))));

        pedidoService.cambiarEstado(pedido.id(), EstadoPedido.CANCELADO);

        assertEquals(5, atst().getStock());
    }
}