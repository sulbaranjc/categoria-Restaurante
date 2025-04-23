package com.sulbaranjc.categoria.controller;

import com.sulbaranjc.categoria.model.Categoria;
import com.sulbaranjc.categoria.repository.CategoriaRepository;
import com.sulbaranjc.categoria.service.MinioService;
import com.sulbaranjc.categoria.dto.CategoriaDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private MinioService minioService;

    // Solo este método necesita cambio
    @GetMapping
    public List<CategoriaDTO> listarCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();
        return categorias.stream().map(categoria -> {
            String urlImagen = minioService.generarPresignedUrl(
                    categoria.getImagen(),
                    60 // duración en minutos
            );
            return new CategoriaDTO(
                    categoria.getId(),
                    categoria.getNombre(),
                    categoria.getDescripcion(),
                    urlImagen
            );
        }).toList();
    }

    // Sin cambios
    @PostMapping
    public ResponseEntity<?> crearCategoria(@RequestBody Categoria categoria) {
        if (categoriaRepository.existsByNombre(categoria.getNombre())) {
            return ResponseEntity.badRequest().body("La categoría ya existe");
        }
        Categoria nuevaCategoria = categoriaRepository.save(categoria);
        return ResponseEntity.ok(nuevaCategoria);
    }

    // Sin cambios
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

    // Sin cambios
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Long id) {
        return categoriaRepository.findById(id).map(categoria -> {
            categoriaRepository.delete(categoria);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

}
