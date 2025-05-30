package com.humanbooster.businesscase.controller;


import com.humanbooster.businesscase.model.Reservation;
import com.humanbooster.businesscase.service.ReservationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@Valid @RequestBody Reservation reservation,
                                               @RequestParam Long idUtilisateurClient,
                                               @RequestParam Long idBorne) {
        try {
            Reservation createdReservation = reservationService.createReservation(reservation, idUtilisateurClient, idBorne);
            return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/utilisateur/{utilisateurId}")
    public ResponseEntity<?> getReservationsByUtilisateurId(@PathVariable Long utilisateurId) {
        try {
            List<Reservation> reservations = reservationService.getReservationsByUtilisateurId(utilisateurId);
            return ResponseEntity.ok(reservations);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/borne/{borneId}")
    public ResponseEntity<?> getReservationsByBorneId(@PathVariable Long borneId) {
        try {
            List<Reservation> reservations = reservationService.getReservationsByBorneId(borneId);
            return ResponseEntity.ok(reservations);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<?> updateReservationStatus(@PathVariable Long id, @RequestBody Map<String, String> updates) {
        String statut = updates.get("statut");
        String noteProprietaire = updates.get("noteProprietaire");

        if (statut == null || statut.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Le statut est requis.");
        }
        try {
            Reservation updatedReservation = reservationService.updateReservationStatus(id, statut, noteProprietaire);
            return ResponseEntity.ok(updatedReservation);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/confirmer-paiement")
    public ResponseEntity<?> confirmPaiement(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        try {
            BigDecimal montantPaye = new BigDecimal(payload.get("montantPaye").toString());
            String transactionId = (String) payload.get("transactionId");

            if (montantPaye == null || transactionId == null) {
                return ResponseEntity.badRequest().body("Le montant payé et l'ID de transaction sont requis.");
            }
            Reservation reservation = reservationService.confirmPaiement(id, montantPaye, transactionId);
            return ResponseEntity.ok(reservation);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Données de paiement invalides: " + e.getMessage());
        }
    }
}