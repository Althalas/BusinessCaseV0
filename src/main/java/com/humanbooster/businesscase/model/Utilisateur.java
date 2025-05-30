package com.humanbooster.businesscase.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

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
    private String motDePasse; // Devrait être stocké haché

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
    private String codeValidationEmail;

    @Column(name = "EmailVerifie", nullable = false)
    private boolean emailVerifie = false;

    @Column(name = "DateInscription", nullable = false)
    private LocalDateTime dateInscription = LocalDateTime.now();

    @Column(name = "Role", nullable = false, length = 50)
    private RoleUtilisateur role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAdresse")
    private Adresse adresse;

    @OneToMany(mappedBy = "proprietaire", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LieuRecharge> lieuxRecharge;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservationsEffectuees;
}
