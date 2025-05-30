package com.humanbooster.businesscase.service;

import com.humanbooster.businesscase.model.Badge;
import com.humanbooster.businesscase.model.UtilisateurBadge; // Utilisé pour attribuer

import java.util.List;
import java.util.Optional;

public interface BadgeService {
    // Gestion des Badges (types)
    Badge createBadge(Badge badge);
    Optional<Badge> getBadgeById(Long id);
    Optional<Badge> getBadgeByNom(String nom);
    List<Badge> getAllBadges();
    Badge updateBadge(Long id, Badge badgeDetails);
    void deleteBadge(Long id); // Attention aux UtilisateurBadges existants

    // Gestion de l'attribution des badges aux utilisateurs
    UtilisateurBadge assignBadgeToUtilisateur(Long idUtilisateur, Long idBadge);
    void revokeBadgeFromUtilisateur(Long idUtilisateur, Long idBadge);
    List<UtilisateurBadge> getBadgesForUtilisateur(Long idUtilisateur);
    List<UtilisateurBadge> getUtilisateursForBadge(Long idBadge); // Moins courant mais possible
}