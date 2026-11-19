package com.brickmotors.ms_usuarios.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brickmotors.ms_usuarios.dto.DetallePedidoResponse;
import com.brickmotors.ms_usuarios.dto.PedidoRequest;
import com.brickmotors.ms_usuarios.dto.PedidoResponse;
import com.brickmotors.ms_usuarios.exception.ReglaNegocioException;
import com.brickmotors.ms_usuarios.exception.ResourceNotFoundException;
import com.brickmotors.ms_usuarios.model.DetallePedido;
import com.brickmotors.ms_usuarios.model.EstadoPedido;
import com.brickmotors.ms_usuarios.model.Pedido;
import com.brickmotors.ms_usuarios.model.Producto;
import com.brickmotors.ms_usuarios.model.Usuario;
import com.brickmotors.ms_usuarios.repository.PedidoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;

    public PedidoService(PedidoRepository pedidoRepository,
                         UsuarioService usuarioService,
                         ProductoService productoService) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioService = usuarioService;
        this.productoService = productoService;
    }

    /**
     * Convierte el carrito en un pedido: valida stock, lo descuenta y guarda
     * una copia del nombre y precio de cada producto. Todo o nada (@Transactional).
     */
    @Transactional
    public PedidoResponse crear(PedidoRequest request) {
        Usuario usuario = usuarioService.buscarEntidad(request.usuarioId());

        // Si el mismo producto viene dos veces en el carrito, se suman las cantidades
        Map<Long, Integer> cantidades = new LinkedHashMap<>();
        request.items().forEach(i -> cantidades.merge(i.productoId(), i.cantidad(), Integer::sum));

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setEstado(EstadoPedido.PENDIENTE);

        int total = 0;
        for (Map.Entry<Long, Integer> item : cantidades.entrySet()) {
            Producto producto = productoService.buscarEntidad(item.getKey());
            int cantidad = item.getValue();

            if (producto.getStock() < cantidad) {
                throw new ReglaNegocioException("Stock insuficiente para '" + producto.getNombre()
                        + "'. Disponible: " + producto.getStock());
            }
            producto.setStock(producto.getStock() - cantidad);

            int descuento = producto.getDescuento() == null ? 0 : producto.getDescuento();
            int subtotal = ProductoService.calcularPrecioFinal(producto.getPrecio(), descuento) * cantidad;

            DetallePedido detalle = new DetallePedido();
            detalle.setProducto(producto);
            detalle.setNombreProducto(producto.getNombre());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setDescuento(descuento);
            detalle.setCantidad(cantidad);
            detalle.setSubtotal(subtotal);
            pedido.agregarDetalle(detalle);

            total += subtotal;
        }
        pedido.setTotal(total);

        Pedido guardado = pedidoRepository.save(pedido);
        log.info("Pedido creado id={} usuario={} total={}", guardado.getId(), usuario.getId(), total);
        return toResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listar(Long usuarioId) {
        List<Pedido> pedidos = (usuarioId == null)
                ? pedidoRepository.findAllByOrderByFechaDesc()
                : pedidoRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);
        return pedidos.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponse obtenerPorId(Long id) {
        return toResponse(buscar(id));
    }

    /** Cambia el estado. Si se cancela, devuelve el stock a los productos. */
    @Transactional
    public PedidoResponse cambiarEstado(Long id, EstadoPedido nuevo) {
        Pedido pedido = buscar(id);
        EstadoPedido actual = pedido.getEstado();

        if (actual == EstadoPedido.CANCELADO || actual == EstadoPedido.ENTREGADO) {
            throw new ReglaNegocioException("El pedido ya está " + actual + " y no se puede modificar.");
        }
        if (nuevo == EstadoPedido.CANCELADO) {
            pedido.getDetalles().forEach(d -> {
                Producto p = d.getProducto();
                p.setStock(p.getStock() + d.getCantidad());
            });
        }
        pedido.setEstado(nuevo);
        log.info("Pedido id={} cambió de {} a {}", id, actual, nuevo);
        return toResponse(pedidoRepository.save(pedido));
    }

    private Pedido buscar(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un pedido con id " + id));
    }

    private PedidoResponse toResponse(Pedido p) {
        List<DetallePedidoResponse> detalles = p.getDetalles().stream()
                .map(d -> new DetallePedidoResponse(d.getProducto().getId(), d.getNombreProducto(),
                        d.getPrecioUnitario(), d.getDescuento(), d.getCantidad(), d.getSubtotal()))
                .toList();
        return new PedidoResponse(p.getId(), p.getUsuario().getId(), p.getUsuario().getNombre(),
                p.getFecha(), p.getEstado(), p.getTotal(), detalles);
    }
}