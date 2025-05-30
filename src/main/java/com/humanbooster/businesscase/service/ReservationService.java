package com.humanbooster.businesscase.service;


import com.humanbooster.businesscase.model.Reservation; // Changement ici
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ReservationService {
    Reservation createReservation(Reservation reservation, Long idUtilisateurClient, Long idBorne); // Changement ici
    Optional<Reservation> getReservationById(Long id); // Changement ici
    List<Reservation> getAllReservations(); // Changement ici
    List<Reservation> getReservationsByUtilisateurId(Long utilisateurId); // Changement ici
    List<Reservation> getReservationsByBorneId(Long borneId); // Changement ici
    Reservation updateReservationStatus(Long id, String statut, String noteProprietaire); // Changement ici
    Reservation confirmPaiement(Long id, BigDecimal montantPaye, String transactionId); // Changement ici
}