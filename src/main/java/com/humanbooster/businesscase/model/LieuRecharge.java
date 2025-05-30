package com.humanbooster.businesscase.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table(name = "LieuRecharge")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LieuRecharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLieu;

    @Column(name = "Nom", nullable = false, length = 150)
    private String nom;

    @Column(name = "InstructionsAcces", columnDefinition = "TEXT")
    private String instructionsAcces;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idProprietaire", nullable = false)
    private Utilisateur proprietaire;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "idAdresse", referencedColumnName = "idAdresse", nullable = false, unique = true)
    private Adresse adresse;

    @OneToMany(mappedBy = "lieuRecharge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Borne> bornes;
}