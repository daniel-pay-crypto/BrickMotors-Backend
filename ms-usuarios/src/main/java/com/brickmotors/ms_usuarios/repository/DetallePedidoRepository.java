package com.brickmotors.ms_usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brickmotors.ms_usuarios.model.DetallePedido;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

    boolean existsByProductoId(Long productoId);
}