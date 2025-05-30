package com.humanbooster.businesscase.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Media")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "borneMedias")
@EqualsAndHashCode(exclude = "borneMedias")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMedia;

    @Column(name = "NomFichier", nullable = false, length = 255)
    private String nomFichier;

    @Column(name = "TypeMedia", nullable = false, length = 20) // Ex: "Photo", "Video"
    private String typeMedia;

    @Column(name = "URL", nullable = false, length = 512)
    private String url;

    @Column(name = "TailleOctets")
    private Integer tailleOctets;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "DateUpload", nullable = false)
    private LocalDateTime dateUpload = LocalDateTime.now();

    @OneToMany(mappedBy = "media", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("media-bornes") // Media est le "parent" dans cette relation avec BorneMedia
    private List<BorneMedia> borneMedias;
}