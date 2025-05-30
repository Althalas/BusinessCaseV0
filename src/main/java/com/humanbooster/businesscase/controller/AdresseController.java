package com.humanbooster.businesscase.controller;


import com.humanbooster.businesscase.model.Adresse;
import com.humanbooster.businesscase.service.AdresseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adresses")
public class AdresseController {

    private final AdresseService adresseService;

    public AdresseController(AdresseService adresseService) {
        this.adresseService = adresseService;
    }

    @PostMapping
    public ResponseEntity<Adresse> createAdresse(@Valid @RequestBody Adresse adresse) {
        Adresse savedAdresse = adresseService.save(adresse);
        return new ResponseEntity<>(savedAdresse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Adresse> getAdresseById(@PathVariable Long id) {
        return adresseService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Adresse>> getAllAdresses() {
        List<Adresse> adresses = adresseService.findAll();
        return ResponseEntity.ok(adresses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Adresse> updateAdresse(@PathVariable Long id, @Valid @RequestBody Adresse adresseDetails) {
        try {
            Adresse updatedAdresse = adresseService.update(id, adresseDetails);
            return ResponseEntity.ok(updatedAdresse);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdresse(@PathVariable Long id) {
        try {
            adresseService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) { // Si utilisée par un utilisateur ou lieu
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}