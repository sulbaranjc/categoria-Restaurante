package com.sulbaranjc.categoria.controller;

import com.sulbaranjc.categoria.model.Categoria;
import com.sulbaranjc.categoria.repository.CategoriaRepository;
import com.sulbaranjc.categoria.service.MinioService;
import com.sulbaranjc.categoria.dto.CategoriaDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private MinioService minioService;

    // SIN CAMBIOS
    @GetMapping
    public List<CategoriaDTO> listarCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();
        return categorias.stream().map(categoria -> {
            String urlImagen = minioService.generarPresignedUrl(
                    categoria.getImagen(),
                    60
            );
            return new CategoriaDTO(
                    categoria.getId(),
                    categoria.getNombre(),
                    categoria.getDescripcion(),
                    urlImagen
            );
        }).toList();
    }

    // SIN CAMBIOS
    @PostMapping
    public ResponseEntity<?> crearCategoria(@RequestBody Categoria categoria) {
        if (categoriaRepository.existsByNombre(categoria.getNombre())) {
            return ResponseEntity.badRequest().body("La categoría ya existe");
        }
        Categoria nuevaCategoria = categoriaRepository.save(categoria);
        return ResponseEntity.ok(nuevaCategoria);
    }

    // SIN CAMBIOS
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCategoria(@PathVariable Long id, @RequestBody Categoria categoriaActualizada) {
        return categoriaRepository.findById(id).map(categoria -> {
            categoria.setNombre(categoriaActualizada.getNombre());
            categoria.setDescripcion(categoriaActualizada.getDescripcion());
            categoria.setImagen(categoriaActualizada.getImagen());
            categoriaRepository.save(categoria);
            return ResponseEntity.ok(categoria);
        }).orElse(ResponseEntity.notFound().build());
    }

    // SIN CAMBIOS
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Long id) {
        return categoriaRepository.findById(id).map(categoria -> {
            categoriaRepository.delete(categoria);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // NUEVO MÉTODO para crear categoría con imagen (multipart/form-data)
    @PostMapping("/crear")
    public ResponseEntity<?> crearCategoriaConImagen(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("imagen") MultipartFile imagen) {

        try {
            if (categoriaRepository.existsByNombre(nombre)) {
                return ResponseEntity.badRequest().body("La categoría ya existe");
            }

            // Generar nombre único para la imagen
            String nombreUnico = UUID.randomUUID().toString() + "-" + imagen.getOriginalFilename();

            // Subir imagen a MinIO
            minioService.subirArchivoConNombre(imagen, nombreUnico);

            // Crear y guardar categoría
            Categoria categoria = new Categoria();
            categoria.setNombre(nombre);
            categoria.setDescripcion(descripcion);
            categoria.setImagen(nombreUnico); // Guardamos solo el nombre único
            categoriaRepository.save(categoria);

            return ResponseEntity.ok("Categoría creada con éxito");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al crear categoría: " + e.getMessage());
        }
    }
}
