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
@Table(name = "Utilisateur_Badge", uniqueConstraints = @UniqueConstraint(columnNames = {"idUtilisateur", "idBadge"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"utilisateur", "badge"})
@EqualsAndHashCode(exclude = {"utilisateur", "badge"})
public class UtilisateurBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUtilisateur", nullable = false)
    @JsonBackReference("utilisateur-badges")
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idBadge", nullable = false)
    @JsonBackReference("badge-utilisateurs")
    private Badge badge;

    @Column(name = "DateObtention", nullable = false)
    private LocalDateTime dateObtention = LocalDateTime.now();
}