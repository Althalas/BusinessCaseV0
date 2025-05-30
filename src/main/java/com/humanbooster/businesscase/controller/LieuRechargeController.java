package com.humanbooster.businesscase.controller;

import com.humanbooster.businesscase.model.LieuRecharge;
import com.humanbooster.businesscase.service.LieuRechargeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lieux-recharge")
public class LieuRechargeController {

    private final LieuRechargeService lieuRechargeService;

    public LieuRechargeController(LieuRechargeService lieuRechargeService) {
        this.lieuRechargeService = lieuRechargeService;
    }

    @PostMapping
    public ResponseEntity<?> createLieuRecharge(@Valid @RequestBody LieuRecharge lieuRecharge,
                                                @RequestParam Long idProprietaire,
                                                @RequestParam Long idAdresse) {
        try {
            LieuRecharge createdLieu = lieuRechargeService.createLieuRecharge(lieuRecharge, idProprietaire, idAdresse);
            return new ResponseEntity<>(createdLieu, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalStateException e) { // Pour conflit d'adresse
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<LieuRecharge> getLieuRechargeById(@PathVariable Long id) {
        return lieuRechargeService.getLieuRechargeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<LieuRecharge>> getAllLieuxRecharge() {
        List<LieuRecharge> lieux = lieuRechargeService.getAllLieuxRecharge();
        return ResponseEntity.ok(lieux);
    }

    @GetMapping("/proprietaire/{idProprietaire}")
    public ResponseEntity<?> getLieuxRechargeByProprietaireId(@PathVariable Long idProprietaire) {
        try {
            List<LieuRecharge> lieux = lieuRechargeService.getLieuxRechargeByProprietaireId(idProprietaire);
            return ResponseEntity.ok(lieux);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLieuRecharge(@PathVariable Long id,
                                                @Valid @RequestBody LieuRecharge lieuRechargeDetails,
                                                @RequestParam(required = false) Long idProprietaire,
                                                @RequestParam(required = false) Long idAdresse) {
        try {
            LieuRecharge updatedLieu = lieuRechargeService.updateLieuRecharge(id, lieuRechargeDetails, idProprietaire, idAdresse);
            return ResponseEntity.ok(updatedLieu);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLieuRecharge(@PathVariable Long id) {
        try {
            lieuRechargeService.deleteLieuRecharge(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}