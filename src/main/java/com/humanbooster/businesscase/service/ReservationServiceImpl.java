package com.humanbooster.businesscase.service;

import com.humanbooster.businesscase.model.Borne;
import com.humanbooster.businesscase.model.Reservation;
import com.humanbooster.businesscase.model.StatutReservation;
import com.humanbooster.businesscase.model.Utilisateur;
import com.humanbooster.businesscase.repository.BorneRepository;
import com.humanbooster.businesscase.repository.ReservationRepository;
import com.humanbooster.businesscase.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final BorneRepository borneRepository;

    private static final String STATUT_EN_ATTENTE = "EnAttenteConfirmation";
    private static final String STATUT_ACCEPTEE = "Acceptee";
    private static final String STATUT_PAYEE = "Payee";


    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  UtilisateurRepository utilisateurRepository,
                                  BorneRepository borneRepository) {
        this.reservationRepository = reservationRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.borneRepository = borneRepository;
    }

    @Override
    public Reservation createReservation(Reservation reservation, Long idUtilisateurClient, Long idBorne) {
        if (reservation.getDateHeureFin().isBefore(reservation.getDateHeureDebut()) ||
                reservation.getDateHeureFin().isEqual(reservation.getDateHeureDebut())) {
            throw new IllegalArgumentException("La date de fin doit être après la date de début.");
        }

        Utilisateur client = utilisateurRepository.findById(idUtilisateurClient)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur client non trouvé: " + idUtilisateurClient));
        Borne borne = borneRepository.findById(idBorne)
                .orElseThrow(() -> new EntityNotFoundException("Borne non trouvée: " + idBorne));

        List<Reservation> conflits = reservationRepository.findByBorneIdBorneAndDateHeureFinAfterAndDateHeureDebutBefore(
                borne.getIdBorne(),
                reservation.getDateHeureDebut(),
                reservation.getDateHeureFin()
        ).stream().filter(r -> STATUT_ACCEPTEE.equals(r.getStatutReservation()) || STATUT_PAYEE.equals(r.getStatutReservation()) || STATUT_EN_ATTENTE.equals(r.getStatutReservation())).toList();

        if (!conflits.isEmpty()) {
            throw new IllegalStateException("La borne n'est pas disponible pour la période sélectionnée.");
        }

        reservation.setIdReservation(null); // S'assurer que c'est une nouvelle réservation
        reservation.setClient(client);
        reservation.setBorne(borne);
        reservation.setDateCreationReservation(LocalDateTime.now());
        reservation.setStatutReservation(StatutReservation.valueOf(STATUT_EN_ATTENTE));

        Duration duree = Duration.between(reservation.getDateHeureDebut(), reservation.getDateHeureFin());
        long heures = duree.toHours();
        if (duree.toMinutesPart() > 0) heures++;

        BigDecimal montantCalcule = borne.getTarifHoraire().multiply(BigDecimal.valueOf(heures));
        reservation.setMontantTotalCalcule(montantCalcule);

        return reservationRepository.save(reservation);
    }

    @Override
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public List<Reservation> getReservationsByUtilisateurId(Long utilisateurId) {
        if (!utilisateurRepository.existsById(utilisateurId)) {
            throw new EntityNotFoundException("Utilisateur non trouvé: " + utilisateurId);
        }
        return reservationRepository.findByClientIdUtilisateur(utilisateurId);
    }

    @Override
    public List<Reservation> getReservationsByBorneId(Long borneId) {
        if (!borneRepository.existsById(borneId)) {
            throw new EntityNotFoundException("Borne non trouvée: " + borneId);
        }
        return reservationRepository.findByBorneIdBorne(borneId);
    }

    @Override
    public Reservation updateReservationStatus(Long id, String statut, String noteProprietaire) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Réservation non trouvée: " + id));

        reservation.setStatutReservation(StatutReservation.valueOf(statut));
        if(noteProprietaire != null) {
            reservation.setNoteProprietaire(noteProprietaire);
        }
        if (STATUT_ACCEPTEE.equals(statut) && reservation.getNumeroFacturation() == null) {
            reservation.setNumeroFacturation("FACT-" + LocalDateTime.now().getYear() + "-" + reservation.getIdReservation());
        }
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation confirmPaiement(Long id, BigDecimal montantPaye, String transactionId) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Réservation non trouvée: " + id));

        if (!STATUT_ACCEPTEE.equals(reservation.getStatutReservation())) {
            throw new IllegalStateException("La réservation doit être acceptée avant de confirmer le paiement.");
        }
        reservation.setMontantReelRegle(montantPaye);
        reservation.setTransactionPaiementId(transactionId);
        reservation.setStatutReservation(StatutReservation.valueOf(STATUT_PAYEE));

        return reservationRepository.save(reservation);
    }
}