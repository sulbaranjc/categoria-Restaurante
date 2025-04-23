package com.sulbaranjc.categoria.config;

import com.sulbaranjc.categoria.model.Rol;
import com.sulbaranjc.categoria.model.Usuario;
import com.sulbaranjc.categoria.repository.RolRepository;
import com.sulbaranjc.categoria.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Configuration
@Slf4j
public class DataInitializer {

    @Bean
    CommandLineRunner initData(RolRepository rolRepository,
                               UsuarioRepository usuarioRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            // Crear roles si no existen
            Rol rolAdmin = rolRepository.findByNombre("ROLE_ADMIN")
                    .orElseGet(() -> rolRepository.save(new Rol("ROLE_ADMIN")));

            Rol rolUser = rolRepository.findByNombre("ROLE_USER")
                    .orElseGet(() -> rolRepository.save(new Rol("ROLE_USER")));

            // Crear usuario admin si no existe
            if (usuarioRepository.findByUsername("admin").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRoles(Set.of(rolAdmin, rolUser));
                usuarioRepository.save(admin);
                log.info("✅ Usuario 'admin' creado.");
            }

            // Crear usuario normal si no existe
            if (usuarioRepository.findByUsername("user").isEmpty()) {
                Usuario user = new Usuario();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setRoles(Set.of(rolUser));
                usuarioRepository.save(user);
                log.info("✅ Usuario 'user' creado.");
            }
        };
    }
}
