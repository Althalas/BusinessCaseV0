package com.humanbooster.businesscase.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "Badge", uniqueConstraints = @UniqueConstraint(columnNames = "NomBadge"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "utilisateurBadges")
@EqualsAndHashCode(exclude = "utilisateurBadges")
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBadge;

    @Column(name = "NomBadge", nullable = false, length = 100)
    private String nomBadge;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "IconeURL", length = 512)
    private String iconeURL;

    @OneToMany(mappedBy = "badge", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("badge-utilisateurs")
    private List<UtilisateurBadge> utilisateurBadges;
}