package com.brickmotors.ms_usuarios.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.brickmotors.ms_usuarios.model.MensajeContacto;

public interface MensajeContactoRepository extends JpaRepository<MensajeContacto, Long> {

    List<MensajeContacto> findAllByOrderByFechaDesc();
}