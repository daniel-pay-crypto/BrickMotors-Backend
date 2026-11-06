package com.brickmotors.ms_usuarios.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brickmotors.ms_usuarios.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    
    
}
