package com.humanbooster.businesscase.controller;


import com.humanbooster.businesscase.model.Vehicule;
import com.humanbooster.businesscase.service.VehiculeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class VehiculeController {

    private final VehiculeService vehiculeService;

    public VehiculeController(VehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    // Créer un véhicule pour un utilisateur spécifique
    @PostMapping("/utilisateurs/{idUtilisateur}/vehicules")
    public ResponseEntity<?> createVehiculeForUtilisateur(@PathVariable Long idUtilisateur,
                                                          @Valid @RequestBody Vehicule vehicule) {
        try {
            Vehicule createdVehicule = vehiculeService.createVehicule(vehicule, idUtilisateur);
            return new ResponseEntity<>(createdVehicule, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/vehicules/{idVehicule}")
    public ResponseEntity<Vehicule> getVehiculeById(@PathVariable Long idVehicule) {
        return vehiculeService.getVehiculeById(idVehicule)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/utilisateurs/{idUtilisateur}/vehicules")
    public ResponseEntity<?> getVehiculesByUtilisateurId(@PathVariable Long idUtilisateur) {
        try {
            List<Vehicule> vehicules = vehiculeService.getVehiculesByProprietaireId(idUtilisateur);
            return ResponseEntity.ok(vehicules);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Endpoint pour admin pour voir tous les véhicules
    @GetMapping("/vehicules")
    // @PreAuthorize("hasRole('ADMINISTRATEUR')")
    public ResponseEntity<List<Vehicule>> getAllVehicules() {
        return ResponseEntity.ok(vehiculeService.getAllVehicules());
    }

    @PutMapping("/vehicules/{idVehicule}")
    public ResponseEntity<?> updateVehicule(@PathVariable Long idVehicule,
                                            @Valid @RequestBody Vehicule vehiculeDetails,
                                            @RequestParam(required = false) Long idProprietaire) { // Pour changer le propriétaire si permis
        try {
            Vehicule updatedVehicule = vehiculeService.updateVehicule(idVehicule, vehiculeDetails, idProprietaire);
            return ResponseEntity.ok(updatedVehicule);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/vehicules/{idVehicule}")
    public ResponseEntity<Void> deleteVehicule(@PathVariable Long idVehicule) {
        try {
            vehiculeService.deleteVehicule(idVehicule);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}