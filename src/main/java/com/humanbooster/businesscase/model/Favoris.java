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
@Table(name = "FavorisUtilisateurBorne", uniqueConstraints = @UniqueConstraint(columnNames = {"idUtilisateur", "idBorne"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"utilisateur", "borne"})
@EqualsAndHashCode(exclude = {"utilisateur", "borne"})
public class Favoris {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUtilisateur", nullable = false)
    @JsonBackReference("utilisateur-favoris")
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idBorne", nullable = false)
    @JsonBackReference("borne-favoris")
    private Borne borne;

    @Column(name = "DateAjout", nullable = false)
    private LocalDateTime dateAjout = LocalDateTime.now();
}