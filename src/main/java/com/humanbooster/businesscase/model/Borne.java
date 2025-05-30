package com.humanbooster.businesscase.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "Borne")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Borne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBorne;

    @Column(name = "Nom", length = 100)
    private String nom;

    @Column(name = "GPS_Latitude", nullable = false, precision = 10, scale = 8)
    private BigDecimal gpsLatitude;

    @Column(name = "GPS_Longitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal gpsLongitude;

    @Column(name = "InstructionsUtilisation", columnDefinition = "TEXT")
    private String instructionsUtilisation;

    @Column(name = "PuissanceKW", nullable = false, precision = 5, scale = 2)
    private BigDecimal puissanceKW;

    @Column(name = "EstMurale")
    private Boolean estMurale;

    @Column(name = "EtatBorne", nullable = false, length = 50)
    private String etatBorne = "Disponible"; // Ex: "Disponible", "En Maintenance"

    @Column(name = "TarifHoraire", nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifHoraire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idLieu", nullable = false)
    private LieuRecharge lieuRecharge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idTypePrise", nullable = false)
    private TypePrise typePrise;

    @OneToMany(mappedBy = "borne", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;
}