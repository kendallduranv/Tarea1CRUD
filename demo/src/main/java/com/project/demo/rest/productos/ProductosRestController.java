package com.project.demo.rest.productos;

import com.project.demo.logic.entity.producto.Producto;
import com.project.demo.logic.entity.producto.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productos")
public class ProductosRestController {
    @Autowired
    private ProductoRepository ProductoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<Producto> getAllProductos() {
        return ProductoRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    public Producto addProducto(@RequestBody Producto producto) {
        return ProductoRepository.save(producto);
    }

    @GetMapping("/{id}")
    public Producto getProductoById(@PathVariable Long id) {
        return ProductoRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @GetMapping("/filterByName/{name}")
    public List<Producto> getProductoById(@PathVariable String nombre) {
        return ProductoRepository.findProductosWithCharacterInName(nombre);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    public Producto updateProducto(@PathVariable Long id, @RequestBody Producto Producto) {
        return ProductoRepository.findById(id)
                .map(existingProducto -> {
                    existingProducto.setNombre(Producto.getNombre());
                    existingProducto.setDescripcion(Producto.getDescripcion());
                    existingProducto.setPrecio(Producto.getPrecio());
                    existingProducto.setCantidadStock(Producto.getCantidadStock());
                    existingProducto.setCategoria(Producto.getCategoria());
                    return ProductoRepository.save(existingProducto);
                })
                .orElseGet(() -> {
                    Producto.setId(id);
                    return ProductoRepository.save(Producto);
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN_ROLE')")
    public ResponseEntity<String> deleteProducto(@PathVariable Long id) {
        Optional<Producto> productoOptional = ProductoRepository.findById(id);

        if (((Optional<?>) productoOptional).isPresent()) {
            ProductoRepository.deleteById(id);
            return new ResponseEntity<>("Producto borrado con éxito", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Producto no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public Producto authenticatedProducto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Producto) authentication.getPrincipal();
    }

}