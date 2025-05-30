package com.humanbooster.businesscase.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Reservation", uniqueConstraints = @UniqueConstraint(columnNames = "NumeroFacturation"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;

    @Column(name = "DateHeureDebut", nullable = false)
    private LocalDateTime dateHeureDebut;

    @Column(name = "DateHeureFin", nullable = false)
    private LocalDateTime dateHeureFin;

    @Column(name = "MontantTotalCalcule", nullable = false, precision = 10, scale = 2)
    private BigDecimal montantTotalCalcule;

    @Column(name = "MontantReelRegle", precision = 10, scale = 2)
    private BigDecimal montantReelRegle;

    @Column(name = "StatutReservation", nullable = false, length = 50)
    private String statutReservation; // Ex: "EnAttenteConfirmation", "Acceptee", "Terminee" [cite: 9, 11, 15]

    @Column(name = "DateCreationReservation", nullable = false)
    private LocalDateTime dateCreationReservation = LocalDateTime.now();

    @Column(name = "NumeroFacturation", length = 100)
    private String numeroFacturation;

    @Column(name = "NoteUtilisateur", columnDefinition = "TEXT")
    private String noteUtilisateur;

    @Column(name = "NoteProprietaire", columnDefinition = "TEXT")
    private String noteProprietaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUtilisateur_Client", nullable = false)
    private Utilisateur client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idBorne", nullable = false)
    private Borne borne;

    @Column(name = "TransactionPaiementId", length = 255)
    private String transactionPaiementId;
}