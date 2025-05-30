package com.humanbooster.businesscase.model;



import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "Message")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"envoyeur", "destinataire", "reservationConcernee"})
@EqualsAndHashCode(exclude = {"envoyeur", "destinataire", "reservationConcernee"})
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMessage;

    @Column(name = "Contenu", nullable = false, columnDefinition = "TEXT")
    private String contenu;

    @Column(name = "DateEnvoi", nullable = false)
    private LocalDateTime dateEnvoi = LocalDateTime.now();

    @Column(name = "EstLu", nullable = false)
    private boolean estLu = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEnvoyeur", nullable = false)
    @JsonBackReference("utilisateur-messagesEnvoyes")
    private Utilisateur envoyeur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDestinataire") // Peut être null
    @JsonBackReference("utilisateur-messagesRecus")
    private Utilisateur destinataire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idReservationConcernee") // Peut être null
    @JsonBackReference("reservation-messages")
    private Reservation reservationConcernee;
}