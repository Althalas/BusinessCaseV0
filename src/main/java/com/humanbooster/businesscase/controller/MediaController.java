package com.humanbooster.businesscase.controller;


import com.humanbooster.businesscase.model.Media;
import com.humanbooster.businesscase.model.BorneMedia;
import com.humanbooster.businesscase.service.MediaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// Pour la gestion des fichiers, il faudrait ajouter @RequestParam("file") MultipartFile file etc.
// et une logique de stockage de fichiers. Ici, nous gérons les métadonnées.

import java.util.List;

@RestController
@RequestMapping("/api")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    // --- Endpoints pour Media (métadonnées) ---
    @PostMapping("/media")
    public ResponseEntity<Media> createMediaMetadata(@Valid @RequestBody Media media) {
        // Dans une vraie application, vous auriez ici la logique de réception du fichier,
        // son stockage, puis la création de l'entité Media avec l'URL/path du fichier.
        // Exemple simplifié :
        // media.setUrl("/uploads/" + media.getNomFichier()); // L'URL serait déterminée par la logique de stockage
        Media createdMedia = mediaService.createMedia(media);
        return new ResponseEntity<>(createdMedia, HttpStatus.CREATED);
    }

    @GetMapping("/media/{id}")
    public ResponseEntity<Media> getMediaById(@PathVariable Long id) {
        return mediaService.getMediaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/media")
    public ResponseEntity<List<Media>> getAllMedia() {
        return ResponseEntity.ok(mediaService.getAllMedia());
    }

    @DeleteMapping("/media/{id}")
    public ResponseEntity<Void> deleteMedia(@PathVariable Long id) {
        try {
            mediaService.deleteMedia(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Endpoints pour lier/délier Media et Borne ---
    @PostMapping("/bornes/{idBorne}/media/{idMedia}")
    public ResponseEntity<?> linkMediaToBorne(@PathVariable Long idBorne,
                                              @PathVariable Long idMedia,
                                              @RequestParam(defaultValue = "false") boolean estImagePrincipale) {
        try {
            BorneMedia borneMedia = mediaService.linkMediaToBorne(idBorne, idMedia, estImagePrincipale);
            return new ResponseEntity<>(borneMedia, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) { // Ex: lien déjà existant géré différemment
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/bornes/{idBorne}/media/{idMedia}")
    public ResponseEntity<String> unlinkMediaFromBorne(@PathVariable Long idBorne, @PathVariable Long idMedia) {
        try {
            mediaService.unlinkMediaFromBorne(idBorne, idMedia);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/bornes/{idBorne}/media")
    public ResponseEntity<?> getMediaForBorne(@PathVariable Long idBorne) {
        try {
            List<BorneMedia> mediaList = mediaService.getMediaForBorne(idBorne);
            return ResponseEntity.ok(mediaList);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/bornes/{idBorne}/media/{idMedia}/set-principal")
    public ResponseEntity<?> setMediaAsPrincipal(@PathVariable Long idBorne, @PathVariable Long idMedia) {
        try {
            BorneMedia borneMedia = mediaService.setMediaAsPrincipalForBorne(idBorne, idMedia);
            return ResponseEntity.ok(borneMedia);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}