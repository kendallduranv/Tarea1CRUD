package com.project.demo.rest.categoria;

import com.project.demo.logic.entity.categoria.Categoria;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.categoria.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaRestController {
    @Autowired
    private CategoriaRepository CategoriaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<Categoria> getAllCategorias() {
        return CategoriaRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole( 'SUPER_ADMIN_ROLE')")
    public Categoria addCategoria(@RequestBody Categoria categoria) {
        return CategoriaRepository.save(categoria);
    }

    @GetMapping("/{id}")
    public Categoria getCategoriaById(@PathVariable Long id) {
        return CategoriaRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @GetMapping("/filterByName/{name}")
    public List<Categoria> getCategoriaById(@PathVariable String nombre) {
        return CategoriaRepository.findCategoriasWithCharacterInName(nombre);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole( 'SUPER_ADMIN_ROLE')")
    public Categoria updateCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        return CategoriaRepository.findById(id)
                .map(existingCategoria -> {
                    existingCategoria.setNombre(categoria.getNombre());
                    existingCategoria.setDescripcion(categoria.getDescripcion());
                    return CategoriaRepository.save(existingCategoria);
                })
                .orElseGet(() -> {
                    categoria.setId(id);
                    return CategoriaRepository.save(categoria);
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole( 'SUPER_ADMIN_ROLE')")
    public void deleteCategoria(@PathVariable Long id) {
        CategoriaRepository.deleteById(id);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public User authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

}