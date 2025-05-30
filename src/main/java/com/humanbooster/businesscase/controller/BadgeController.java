package com.humanbooster.businesscase.controller;


import com.humanbooster.businesscase.model.Badge;
import com.humanbooster.businesscase.model.UtilisateurBadge;
import com.humanbooster.businesscase.service.BadgeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// import org.springframework.security.access.prepost.PreAuthorize; // Pour admin

import java.util.List;

@RestController
@RequestMapping("/api")
// @PreAuthorize("hasRole('ADMINISTRATEUR')") // Sécuriser toutes les routes pour les admins
public class BadgeController {

    private final BadgeService badgeService;

    public BadgeController(BadgeService badgeService) {
        this.badgeService = badgeService;
    }

    // --- Gestion des types de Badges (typiquement Admin) ---
    @PostMapping("/badges")
    public ResponseEntity<?> createBadge(@Valid @RequestBody Badge badge) {
        try {
            Badge createdBadge = badgeService.createBadge(badge);
            return new ResponseEntity<>(createdBadge, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/badges/{id}")
    public ResponseEntity<Badge> getBadgeById(@PathVariable Long id) {
        return badgeService.getBadgeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/badges")
    public ResponseEntity<List<Badge>> getAllBadges() {
        return ResponseEntity.ok(badgeService.getAllBadges());
    }

    @PutMapping("/badges/{id}")
    public ResponseEntity<?> updateBadge(@PathVariable Long id, @Valid @RequestBody Badge badgeDetails) {
        try {
            Badge updatedBadge = badgeService.updateBadge(id, badgeDetails);
            return ResponseEntity.ok(updatedBadge);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/badges/{id}")
    public ResponseEntity<String> deleteBadge(@PathVariable Long id) {
        try {
            badgeService.deleteBadge(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // --- Gestion de l'attribution des Badges aux Utilisateurs (typiquement Admin) ---
    @PostMapping("/utilisateurs/{idUtilisateur}/badges/{idBadge}")
    public ResponseEntity<?> assignBadgeToUtilisateur(@PathVariable Long idUtilisateur, @PathVariable Long idBadge) {
        try {
            UtilisateurBadge utilisateurBadge = badgeService.assignBadgeToUtilisateur(idUtilisateur, idBadge);
            return new ResponseEntity<>(utilisateurBadge, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/utilisateurs/{idUtilisateur}/badges/{idBadge}")
    public ResponseEntity<String> revokeBadgeFromUtilisateur(@PathVariable Long idUtilisateur, @PathVariable Long idBadge) {
        try {
            badgeService.revokeBadgeFromUtilisateur(idUtilisateur, idBadge);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/utilisateurs/{idUtilisateur}/badges")
    public ResponseEntity<?> getBadgesForUtilisateur(@PathVariable Long idUtilisateur) {
        try {
            List<UtilisateurBadge> badges = badgeService.getBadgesForUtilisateur(idUtilisateur);
            return ResponseEntity.ok(badges);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}