package com.humanbooster.businesscase.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

    @Entity
    @Table(name = "Adresse")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Adresse {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long idAdresse;

        @Column(name = "NumeroRue", length = 50)
        private String numeroRue;

        @Column(name = "Rue", nullable = false, length = 255)
        private String rue;

        @Column(name = "Complement", length = 255)
        private String complement;

        @Column(name = "Etage", length = 50)
        private String etage;

        @Column(name = "CodePostal", nullable = false, length = 10)
        private String codePostal;

        @Column(name = "Ville", nullable = false, length = 100)
        private String ville;

        @Column(name = "Region", length = 100)
        private String region;

        @Column(name = "Pays", nullable = false, length = 100)
        private String pays;

        @Column(name = "Latitude", precision = 10, scale = 8)
        private BigDecimal latitude;

        @Column(name = "Longitude", precision = 11, scale = 8)
        private BigDecimal longitude;
    }
