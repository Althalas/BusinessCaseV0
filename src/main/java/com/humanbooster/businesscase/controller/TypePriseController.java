package com.humanbooster.businesscase.controller;


import com.humanbooster.businesscase.model.TypePrise;
import com.humanbooster.businesscase.service.TypePriseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// import org.springframework.security.access.prepost.PreAuthorize; // Pour sécuriser (admin seulement)

import java.util.List;

@RestController
@RequestMapping("/api/types-prise")

public class TypePriseController {

    private final TypePriseService typePriseService;

    public TypePriseController(TypePriseService typePriseService) {
        this.typePriseService = typePriseService;
    }

    @PostMapping
    public ResponseEntity<TypePrise> createTypePrise(@Valid @RequestBody TypePrise typePrise) {
        // Vérifier l'unicité du nom si nécessaire avant de sauvegarder
        if (typePriseService.findByNom(typePrise.getNomTypePrise()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Ou retourner une erreur spécifique
        }
        TypePrise savedTypePrise = typePriseService.save(typePrise);
        return new ResponseEntity<>(savedTypePrise, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypePrise> getTypePriseById(@PathVariable Long id) {
        return typePriseService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nom/{nom}")
    public ResponseEntity<TypePrise> getTypePriseByNom(@PathVariable String nom) {
        return typePriseService.findByNom(nom)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TypePrise>> getAllTypesPrise() {
        List<TypePrise> typesPrise = typePriseService.findAll();
        return ResponseEntity.ok(typesPrise);
    }

    // La mise à jour d'un TypePrise est généralement moins fréquente.
    // On pourrait la skipper ou l'implémenter si besoin.

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypePrise(@PathVariable Long id) {
        try {
            typePriseService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}