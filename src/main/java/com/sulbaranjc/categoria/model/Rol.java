package com.sulbaranjc.categoria.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    // 🔥 Eliminamos la relación inversa para evitar ciclos y errores de concurrencia
    // Si no necesitas acceder a los usuarios desde el rol, no es necesario
    // @ManyToMany(mappedBy = "roles")
    // private Set<Usuario> usuarios;

    // Constructor personalizado para solo nombre
    public Rol(String nombre) {
        this.nombre = nombre;
    }
}
