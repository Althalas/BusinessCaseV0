package com.humanbooster.businesscase.controller;


import com.humanbooster.businesscase.model.Borne;
import com.humanbooster.businesscase.model.Favoris;
import com.humanbooster.businesscase.service.FavorisService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/utilisateurs/{idUtilisateur}/favoris")
public class FavorisController {

    private final FavorisService favorisService;

    public FavorisController(FavorisService favorisService) {
        this.favorisService = favorisService;
    }

    @PostMapping("/{idBorne}")
    public ResponseEntity<?> addFavori(@PathVariable Long idUtilisateur, @PathVariable Long idBorne) {
        try {
            Favoris favori = favorisService.addFavori(idUtilisateur, idBorne);
            return new ResponseEntity<>(favori, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/{idBorne}")
    public ResponseEntity<String> removeFavori(@PathVariable Long idUtilisateur, @PathVariable Long idBorne) {
        try {
            favorisService.removeFavori(idUtilisateur, idBorne);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getFavorisByUtilisateur(@PathVariable Long idUtilisateur) {
        try {
            // Option 1: retourner les objets Favoris (avec détails de la borne)
            // List<Favoris> favoris = favorisService.getFavorisByUtilisateur(idUtilisateur);
            // Option 2: retourner directement la liste des Bornes favorites
            List<Borne> bornesFavorites = favorisService.getBornesFavoritesByUtilisateur(idUtilisateur);
            return ResponseEntity.ok(bornesFavorites);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{idBorne}/check")
    public ResponseEntity<Map<String, Boolean>> checkFavori(@PathVariable Long idUtilisateur, @PathVariable Long idBorne) {
        boolean isFavori = favorisService.isBorneInFavoris(idUtilisateur, idBorne);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isFavori", isFavori);
        return ResponseEntity.ok(response);
    }
}