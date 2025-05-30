package com.humanbooster.businesscase.service;


import com.humanbooster.businesscase.model.Adresse;
import com.humanbooster.businesscase.model.Utilisateur;
import com.humanbooster.businesscase.model.RoleUtilisateur;
import com.humanbooster.businesscase.repository.AdresseRepository;
import com.humanbooster.businesscase.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
// import org.springframework.security.crypto.password.PasswordEncoder; // Si vous utilisez Spring Security
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final AdresseRepository adresseRepository;
    // private final PasswordEncoder passwordEncoder; // Pour hacher le mot de passe

    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository,
                                  AdresseRepository adresseRepository
            /*, PasswordEncoder passwordEncoder */) {
        this.utilisateurRepository = utilisateurRepository;
        this.adresseRepository = adresseRepository;
        // this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Utilisateur save(Utilisateur utilisateur, Long idAdresse) {
        // Gérer l'association Adresse
        if (idAdresse != null) {
            Adresse adresse = adresseRepository.findById(idAdresse)
                    .orElseThrow(() -> new EntityNotFoundException("Adresse non trouvée avec l'id: " + idAdresse));
            utilisateur.setAdresse(adresse);
        }

        // Hachage du mot de passe avant sauvegarde (important pour la sécurité)
        // if (utilisateur.getMotDePasse() != null && !utilisateur.getMotDePasse().isEmpty()) {
        //     utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        // }

        if (utilisateur.getDateInscription() == null) {
            utilisateur.setDateInscription(LocalDateTime.now());
        }
        // L'ID sera généré automatiquement si null
        utilisateur.setIdUtilisateur(null);
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public Optional<Utilisateur> findById(Long id) {
        return utilisateurRepository.findById(id);
    }

    @Override
    public Optional<Utilisateur> findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    @Override
    public List<Utilisateur> findAll() {
        return utilisateurRepository.findAll();
    }

    @Override
    public Utilisateur update(Long id, Utilisateur utilisateurDetails, Long newIdAdresse) {
        Utilisateur existingUtilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec l'id: " + id));

        existingUtilisateur.setNom(utilisateurDetails.getNom());
        existingUtilisateur.setPrenom(utilisateurDetails.getPrenom());
        existingUtilisateur.setPseudo(utilisateurDetails.getPseudo());
        existingUtilisateur.setEmail(utilisateurDetails.getEmail()); // Attention aux contraintes d'unicité
        existingUtilisateur.setTelephone(utilisateurDetails.getTelephone());
        existingUtilisateur.setDateDeNaissance(utilisateurDetails.getDateDeNaissance());
        existingUtilisateur.setIban(utilisateurDetails.getIban());
        existingUtilisateur.setEstBanni(utilisateurDetails.isEstBanni());
        existingUtilisateur.setEnVacances(utilisateurDetails.isEnVacances());
        existingUtilisateur.setEmailVerifie(utilisateurDetails.isEmailVerifie());
        existingUtilisateur.setRole(utilisateurDetails.getRole()); // Mettre à jour le rôle

        // Ne pas mettre à jour le mot de passe de cette manière,
        // avoir une méthode dédiée pour le changement de mot de passe.
        // if (utilisateurDetails.getMotDePasse() != null && !utilisateurDetails.getMotDePasse().isEmpty()) {
        //    existingUtilisateur.setMotDePasse(passwordEncoder.encode(utilisateurDetails.getMotDePasse()));
        // }

        if (newIdAdresse != null) {
            Adresse adresse = adresseRepository.findById(newIdAdresse)
                    .orElseThrow(() -> new EntityNotFoundException("Adresse non trouvée avec l'id: " + newIdAdresse));
            existingUtilisateur.setAdresse(adresse);
        } else {
            existingUtilisateur.setAdresse(null); // Permettre de dissocier l'adresse
        }

        return utilisateurRepository.save(existingUtilisateur);
    }

    @Override
    public void deleteById(Long id) {
        Utilisateur utilisateur = findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec l'id: " + id));
        // Ajouter une logique pour vérifier les dépendances avant suppression
        // par exemple, si l'utilisateur a des lieux de recharge, des réservations, etc.
        // if (!utilisateur.getLieuxRecharge().isEmpty() || !utilisateur.getReservationsEffectuees().isEmpty()) {
        //    throw new IllegalStateException("L'utilisateur ne peut être supprimé car il a des données associées.");
        // }
        utilisateurRepository.deleteById(id);
    }

    @Override
    public Utilisateur changerRole(Long id, RoleUtilisateur nouveauRole) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec l'id: " + id));
        utilisateur.setRole(nouveauRole);
        return utilisateurRepository.save(utilisateur);
    }
}