package com.project.demo.logic.entity.producto;

import com.project.demo.logic.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long>  {
    @Query("SELECT u FROM Producto u WHERE LOWER(u.nombre) LIKE %?1%")
    List<Producto> findProductosWithCharacterInName(String character);

    @Query("SELECT u FROM Producto u WHERE u.nombre = ?1")
    Optional<Producto> findByName(String name);


}
