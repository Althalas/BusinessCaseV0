package com.humanbooster.businesscase.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "LieuRecharge")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"proprietaire", "bornes"})
@EqualsAndHashCode(exclude = {"proprietaire", "bornes"})
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
    @JsonBackReference("utilisateur-lieux")
    private Utilisateur proprietaire;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "idAdresse", referencedColumnName = "idAdresse", nullable = false, unique = true)
    private Adresse adresse;

    @OneToMany(mappedBy = "lieuRecharge", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("lieu-bornes")
    private List<Borne> bornes;
}