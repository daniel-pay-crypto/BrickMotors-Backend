package com.brickmotors.ms_usuarios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brickmotors.ms_usuarios.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findAllByOrderByFechaDesc();

    List<Pedido> findByUsuarioIdOrderByFechaDesc(Long usuarioId);

    boolean existsByUsuarioId(Long usuarioId);
}