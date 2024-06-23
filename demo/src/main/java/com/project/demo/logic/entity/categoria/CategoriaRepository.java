package com.project.demo.logic.entity.categoria;

import com.project.demo.logic.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>  {
    @Query("SELECT u FROM Categoria  u WHERE LOWER(u.nombre) LIKE %?1%")
    List<Categoria> findCategoriasWithCharacterInName(String character);

    @Query("SELECT u FROM Categoria u WHERE u.nombre = ?1")
    Optional<Categoria> findByName(String nombre);


}
