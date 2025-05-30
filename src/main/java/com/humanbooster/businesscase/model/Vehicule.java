package com.humanbooster.businesscase.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "Vehicule", uniqueConstraints = @UniqueConstraint(columnNames = "PlaqueImmatriculation"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "proprietaire")
@EqualsAndHashCode(exclude = "proprietaire")
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVehicule;

    @Column(name = "PlaqueImmatriculation", nullable = false, length = 20)
    private String plaqueImmatriculation;

    @Column(name = "Marque", length = 50)
    private String marque;

    @Column(name = "Modele", length = 50)
    private String modele;

    @Column(name = "Annee")
    private Integer annee;

    @Column(name = "CapaciteBatterieKWh", precision = 6, scale = 2)
    private BigDecimal capaciteBatterieKWh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUtilisateur_Proprietaire", nullable = false)
    @JsonBackReference("utilisateur-vehicules")
    private Utilisateur proprietaire;
}