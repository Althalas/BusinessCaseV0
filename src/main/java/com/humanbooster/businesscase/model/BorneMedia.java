package com.humanbooster.businesscase.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "Borne_Media", uniqueConstraints = @UniqueConstraint(columnNames = {"idBorne", "idMedia"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"borne", "media"})
@EqualsAndHashCode(exclude = {"borne", "media"})
public class BorneMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Ou une clé composite si vous préférez
    private Long id; // Clé primaire simple pour l'entité de jointure

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idBorne", nullable = false)
    @JsonBackReference("borne-medias")
    private Borne borne;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idMedia", nullable = false)
    @JsonBackReference("media-bornes")
    private Media media;

    @Column(name = "EstImagePrincipale", nullable = false)
    private boolean estImagePrincipale = false;
}