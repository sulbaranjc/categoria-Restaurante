package com.sulbaranjc.categoria.dto;

public class CategoriaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String imagenUrl;

    public CategoriaDTO(Long id, String nombre, String descripcion, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
    }

    // Getters
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getImagenUrl() { return imagenUrl; }
}
