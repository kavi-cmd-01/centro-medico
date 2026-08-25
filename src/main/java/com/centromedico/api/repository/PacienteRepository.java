package com.centromedico.api.repository;

import com.centromedico.api.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    // Gracias a JpaRepository, aquí ya tenemos gratis los métodos:
    // save(), findAll(), findById(), deleteById()... ¡Sin escribir una sola línea de SQL!
}