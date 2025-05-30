package com.humanbooster.businesscase.service;



import com.humanbooster.businesscase.model.Utilisateur; // Changement ici
import com.humanbooster.businesscase.model.RoleUtilisateur;

import java.util.List;
import java.util.Optional;

public interface UtilisateurService {
    Utilisateur save(Utilisateur utilisateur, Long idAdresse); // Changement ici, idAdresse pour lier
    Optional<Utilisateur> findById(Long id); // Changement ici
    Optional<Utilisateur> findByEmail(String email); // Changement ici
    List<Utilisateur> findAll(); // Changement ici
    Utilisateur update(Long id, Utilisateur utilisateurDetails, Long newIdAdresse); // Changement ici
    void deleteById(Long id);
    Utilisateur changerRole(Long id, RoleUtilisateur nouveauRole);
}