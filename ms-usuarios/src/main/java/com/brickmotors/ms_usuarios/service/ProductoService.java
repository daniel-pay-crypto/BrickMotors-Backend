package com.brickmotors.ms_usuarios.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brickmotors.ms_usuarios.dto.ProductoRequest;
import com.brickmotors.ms_usuarios.dto.ProductoResponse;
import com.brickmotors.ms_usuarios.exception.DuplicateResourceException;
import com.brickmotors.ms_usuarios.exception.ReglaNegocioException;
import com.brickmotors.ms_usuarios.exception.ResourceNotFoundException;
import com.brickmotors.ms_usuarios.model.Producto;
import com.brickmotors.ms_usuarios.repository.DetallePedidoRepository;
import com.brickmotors.ms_usuarios.repository.ProductoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductoService {

    /** Igual que imagenPorDefecto en AdminProductos.jsx */
    public static final String IMAGEN_POR_DEFECTO = "/img/legos.jpg";

    private final ProductoRepository productoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final CategoriaService categoriaService;

    public ProductoService(ProductoRepository productoRepository,
                           DetallePedidoRepository detallePedidoRepository,
                           CategoriaService categoriaService) {
        this.productoRepository = productoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.categoriaService = categoriaService;
    }

    /** Equivalente a calcularPrecioFinal de legosData.js */
    public static int calcularPrecioFinal(int precio, int descuento) {
        return (int) Math.round(precio * (100 - descuento) / 100.0);
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listar(Long categoriaId) {
        List<Producto> productos = (categoriaId == null)
                ? productoRepository.findAll(Sort.by("id"))
                : productoRepository.findByCategoriaIdOrderByIdAsc(categoriaId);
        return productos.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        return toResponse(buscarEntidad(id));
    }

    @Transactional
    public ProductoResponse crear(ProductoRequest request) {
        if (productoRepository.existsByCodigoIgnoreCase(request.codigo().trim())) {
            throw new DuplicateResourceException("codigo", "Ese código ya existe.");
        }
        Producto producto = new Producto();
        copiarDatos(producto, request);
        Producto guardado = productoRepository.save(producto);
        log.info("Producto creado id={} codigo={}", guardado.getId(), guardado.getCodigo());
        return toResponse(guardado);
    }

    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        Producto producto = buscarEntidad(id);
        if (productoRepository.existsByCodigoIgnoreCaseAndIdNot(request.codigo().trim(), id)) {
            throw new DuplicateResourceException("codigo", "Ese código ya existe.");
        }
        copiarDatos(producto, request);
        return toResponse(productoRepository.save(producto));
    }

    @Transactional
    public void eliminar(Long id) {
        Producto producto = buscarEntidad(id);
        if (detallePedidoRepository.existsByProductoId(id)) {
            throw new ReglaNegocioException(
                    "No se puede eliminar un producto que ya aparece en pedidos. Deja su stock en 0.");
        }
        productoRepository.delete(producto);
        log.info("Producto eliminado id={}", id);
    }

    /** Usado también por PedidoService. */
    @Transactional(readOnly = true)
    public Producto buscarEntidad(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un producto con id " + id));
    }

    private void copiarDatos(Producto p, ProductoRequest r) {
        p.setCodigo(r.codigo().trim().toUpperCase());
        p.setNombre(r.nombre().trim());
        p.setDescripcion(r.descripcion());
        p.setCategoria(categoriaService.buscarEntidad(r.categoriaId()));
        p.setPrecio(r.precio());
        p.setStock(r.stock());
        p.setDescuento(r.descuento() == null ? 0 : r.descuento());
        p.setImagen(r.imagen() == null || r.imagen().isBlank() ? IMAGEN_POR_DEFECTO : r.imagen().trim());
    }

    private ProductoResponse toResponse(Producto p) {
        int descuento = p.getDescuento() == null ? 0 : p.getDescuento();
        return new ProductoResponse(
                p.getId(), p.getCodigo(), p.getNombre(), p.getDescripcion(),
                p.getCategoria().getId(), p.getCategoria().getNombre(),
                p.getPrecio(), p.getStock(), descuento, descuento > 0,
                calcularPrecioFinal(p.getPrecio(), descuento), p.getImagen());
    }
}