package com.humanbooster.businesscase.model;

import com.humanbooster.businesscase.model.StatutReservation;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Reservation", uniqueConstraints = @UniqueConstraint(columnNames = "NumeroFacturation"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"client", "borne", "messages"})
@EqualsAndHashCode(exclude = {"client", "borne", "messages"})
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;

    // ... autres champs simples ...
    @Column(name = "DateHeureDebut", nullable = false)
    private LocalDateTime dateHeureDebut;

    @Column(name = "DateHeureFin", nullable = false)
    private LocalDateTime dateHeureFin;

    @Column(name = "MontantTotalCalcule", nullable = false, precision = 10, scale = 2)
    private BigDecimal montantTotalCalcule;

    @Column(name = "MontantReelRegle", precision = 10, scale = 2)
    private BigDecimal montantReelRegle;

    @Enumerated(EnumType.STRING)
    @Column(name = "StatutReservation", nullable = false, length = 50)
    private StatutReservation statutReservation;

    @Column(name = "DateCreationReservation", nullable = false)
    private LocalDateTime dateCreationReservation = LocalDateTime.now();

    @Column(name = "NumeroFacturation", length = 100)
    private String numeroFacturation;

    @Column(name = "NoteUtilisateur", columnDefinition = "TEXT")
    private String noteUtilisateur;

    @Column(name = "NoteProprietaire", columnDefinition = "TEXT")
    private String noteProprietaire;

    @Column(name = "TransactionPaiementId", length = 255)
    private String transactionPaiementId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUtilisateur_Client", nullable = false)
    @JsonBackReference("utilisateur-reservationsEffectuees")
    private Utilisateur client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idBorne", nullable = false)
    @JsonBackReference("borne-reservations")
    private Borne borne;

    @OneToMany(mappedBy = "reservationConcernee", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("reservation-messages")
    private List<Message> messages;
}