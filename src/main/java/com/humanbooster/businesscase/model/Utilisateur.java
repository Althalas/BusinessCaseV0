package com.humanbooster.businesscase.model;


import com.humanbooster.businesscase.model.RoleUtilisateur;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Utilisateur", uniqueConstraints = {
        @UniqueConstraint(columnNames = "Pseudo"),
        @UniqueConstraint(columnNames = "Email")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"lieuxRecharge", "reservationsEffectuees", "vehicules", "messagesEnvoyes", "messagesRecus", "utilisateurBadges", "favoris"}) // Pour éviter les problèmes de toString en boucle
@EqualsAndHashCode(exclude = {"lieuxRecharge", "reservationsEffectuees", "vehicules", "messagesEnvoyes", "messagesRecus", "utilisateurBadges", "favoris"}) // idem pour equals/hashcode
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUtilisateur;

    @Column(name = "Nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "Prenom", nullable = false, length = 100)
    private String prenom;

    @Column(name = "Pseudo", length = 50)
    private String pseudo;

    @Column(name = "Email", nullable = false, length = 255)
    private String email;

    @Column(name = "MotDePasse", nullable = false, length = 255)
    @JsonIgnore // Ne jamais sérialiser le mot de passe
    private String motDePasse;

    @Column(name = "Telephone", length = 20)
    private String telephone;

    @Column(name = "DateDeNaissance")
    private LocalDate dateDeNaissance;

    @Column(name = "IBAN", length = 34)
    private String iban;

    @Column(name = "EstBanni", nullable = false)
    private boolean estBanni = false;

    @Column(name = "EnVacances", nullable = false)
    private boolean enVacances = false;

    @Column(name = "CodeValidationEmail", length = 100)
    @JsonIgnore
    private String codeValidationEmail;

    @Column(name = "EmailVerifie", nullable = false)
    private boolean emailVerifie = false;

    @Column(name = "DateInscription", nullable = false)
    private LocalDateTime dateInscription = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "Role", nullable = false, length = 50)
    private RoleUtilisateur role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAdresse")
    // Si Adresse avait une liste d'utilisateurs, on mettrait @JsonBackReference ici
    private Adresse adresse;

    @OneToMany(mappedBy = "proprietaire", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("utilisateur-lieux")
    private List<LieuRecharge> lieuxRecharge;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("utilisateur-reservationsEffectuees")
    private List<Reservation> reservationsEffectuees;

    @OneToMany(mappedBy = "proprietaire", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("utilisateur-vehicules")
    private List<Vehicule> vehicules;

    @OneToMany(mappedBy = "envoyeur", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("utilisateur-messagesEnvoyes")
    private List<Message> messagesEnvoyes;

    @OneToMany(mappedBy = "destinataire", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("utilisateur-messagesRecus")
    private List<Message> messagesRecus;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("utilisateur-badges")
    private List<UtilisateurBadge> utilisateurBadges;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("utilisateur-favoris")
    private List<Favoris> favoris;
}