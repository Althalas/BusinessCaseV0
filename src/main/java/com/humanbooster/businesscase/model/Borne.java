package com.humanbooster.businesscase.model;


import com.humanbooster.businesscase.model.EtatBorne;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "Borne")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"lieuRecharge", "reservations", "borneMedias", "favoris"})
@EqualsAndHashCode(exclude = {"lieuRecharge", "reservations", "borneMedias", "favoris"})
public class Borne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBorne;

    @Column(name = "Nom", length = 100)
    private String nom;

    // ... autres champs simples ...
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

    @Enumerated(EnumType.STRING)
    @Column(name = "EtatBorne", nullable = false, length = 50)
    private EtatBorne etatBorne = EtatBorne.DISPONIBLE;

    @Column(name = "TarifHoraire", nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifHoraire;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idLieu", nullable = false)
    @JsonBackReference("lieu-bornes")
    private LieuRecharge lieuRecharge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idTypePrise", nullable = false)
    private TypePrise typePrise; // Généralement pas de @JsonBackReference ici si TypePrise n'a pas de List<Borne>

    @OneToMany(mappedBy = "borne", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("borne-reservations")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "borne", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("borne-medias")
    private List<BorneMedia> borneMedias;

    @OneToMany(mappedBy = "borne", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("borne-favoris")
    private List<Favoris> favoris;

}