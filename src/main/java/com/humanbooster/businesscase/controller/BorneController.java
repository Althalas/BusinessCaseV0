package com.humanbooster.businesscase.controller;


import com.humanbooster.businesscase.model.Borne;
import com.humanbooster.businesscase.service.BorneService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bornes")
public class BorneController {

    private final BorneService borneService;

    public BorneController(BorneService borneService) {
        this.borneService = borneService;
    }


    @PostMapping
    public ResponseEntity<Borne> createBorne(@Valid @RequestBody Borne borne,
                                             @RequestParam Long idLieu,
                                             @RequestParam Long idTypePrise) {
        try {

            borne.setIdBorne(null);
            Borne createdBorne = borneService.createBorne(borne, idLieu, idTypePrise);
            return new ResponseEntity<>(createdBorne, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Ou un message d'erreur
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Borne> getBorneById(@PathVariable Long id) {
        return borneService.getBorneById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Borne>> getAllBornes() {
        List<Borne> bornes = borneService.getAllBornes();
        return ResponseEntity.ok(bornes);
    }

    @GetMapping("/lieu/{idLieu}")
    public ResponseEntity<List<Borne>> getBornesByLieuId(@PathVariable Long idLieu) {
        try {
            List<Borne> bornes = borneService.getBornesByLieuId(idLieu);
            return ResponseEntity.ok(bornes);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Borne> updateBorne(@PathVariable Long id,
                                             @Valid @RequestBody Borne borneDetails,
                                             @RequestParam(required = false) Long idLieu,
                                             @RequestParam(required = false) Long idTypePrise) {
        try {
            Borne updatedBorne = borneService.updateBorne(id, borneDetails, idLieu, idTypePrise);
            return ResponseEntity.ok(updatedBorne);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorne(@PathVariable Long id) {
        try {
            borneService.deleteBorne(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}