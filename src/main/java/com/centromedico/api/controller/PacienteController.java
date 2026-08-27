package com.centromedico.backend.controller;

import com.centromedico.backend.model.Paciente;
import com.centromedico.backend.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping
    public ResponseEntity<List<Paciente>> obtenerTodos() {
        return ResponseEntity.ok(pacienteRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> crearPaciente(@RequestBody Paciente paciente) {
        try {
            paciente.setId(null);
            Paciente guardado = pacienteRepository.save(paciente);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPaciente(@PathVariable Long id, @RequestBody Paciente pacienteDetalles) {
        return pacienteRepository.findById(id).map(paciente -> {
            paciente.setNombre(pacienteDetalles.getNombre());
            paciente.setApellido(pacienteDetalles.getApellido());
            paciente.setDni(pacienteDetalles.getDni());
            paciente.setTelefono(pacienteDetalles.getTelefono());
            paciente.setEmail(pacienteDetalles.getEmail());
            return ResponseEntity.ok(pacienteRepository.save(paciente));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPaciente(@PathVariable Long id) {
        if (!pacienteRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El paciente no existe.");
        }
        try {
            pacienteRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("No se pudo eliminar el paciente. Verifica que no tenga registros asociados.");
        }
    }
}