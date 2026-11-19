package com.brickmotors.ms_usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brickmotors.ms_usuarios.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}