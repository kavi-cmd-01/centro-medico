package com.centromedico.api.controller;

import com.centromedico.api.model.Paciente;
import com.centromedico.api.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping
    public ResponseEntity<List<Paciente>> obtenerTodos() {
        try {
            List<Paciente> lista = pacienteRepository.findAll();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> crearPaciente(@RequestBody Paciente paciente) {
        try {
            paciente.setId(null); // Asegura que se interprete como inserción nueva
            Paciente guardado = pacienteRepository.save(paciente);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear paciente: " + e.getMessage());
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
            return ResponseEntity.notFound().build();
        }
        try {
            pacienteRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el paciente");
        }
    }
}