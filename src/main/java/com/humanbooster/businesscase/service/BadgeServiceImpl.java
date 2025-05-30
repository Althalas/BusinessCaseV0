package com.humanbooster.businesscase.service;


import com.humanbooster.businesscase.model.Badge;
import com.humanbooster.businesscase.model.Utilisateur;
import com.humanbooster.businesscase.model.UtilisateurBadge;
import com.humanbooster.businesscase.repository.BadgeRepository;
import com.humanbooster.businesscase.repository.UtilisateurRepository;
import com.humanbooster.businesscase.repository.UtilisateurBadgeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BadgeServiceImpl implements BadgeService {

    private final BadgeRepository badgeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurBadgeRepository utilisateurBadgeRepository;

    public BadgeServiceImpl(BadgeRepository badgeRepository,
                            UtilisateurRepository utilisateurRepository,
                            UtilisateurBadgeRepository utilisateurBadgeRepository) {
        this.badgeRepository = badgeRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurBadgeRepository = utilisateurBadgeRepository;
    }

    @Override
    public Badge createBadge(Badge badge) {
        badge.setIdBadge(null); // Assurer création
        // Vérifier l'unicité du nom du badge si nécessaire
        if (badgeRepository.findByNomBadge(badge.getNomBadge()).isPresent()) {
            throw new IllegalStateException("Un badge avec le nom '" + badge.getNomBadge() + "' existe déjà.");
        }
        return badgeRepository.save(badge);
    }

    @Override
    public Optional<Badge> getBadgeById(Long id) {
        return badgeRepository.findById(id);
    }

    @Override
    public Optional<Badge> getBadgeByNom(String nom) {
        return badgeRepository.findByNomBadge(nom);
    }

    @Override
    public List<Badge> getAllBadges() {
        return badgeRepository.findAll();
    }

    @Override
    public Badge updateBadge(Long id, Badge badgeDetails) {
        Badge existingBadge = badgeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Badge non trouvé: " + id));
        // Vérifier l'unicité du nom si modifié
        if (badgeDetails.getNomBadge() != null && !badgeDetails.getNomBadge().equals(existingBadge.getNomBadge())) {
            if (badgeRepository.findByNomBadge(badgeDetails.getNomBadge()).filter(b -> !b.getIdBadge().equals(id)).isPresent()) {
                throw new IllegalStateException("Un autre badge avec le nom '" + badgeDetails.getNomBadge() + "' existe déjà.");
            }
        }
        existingBadge.setNomBadge(badgeDetails.getNomBadge());
        existingBadge.setDescription(badgeDetails.getDescription());
        existingBadge.setIconeURL(badgeDetails.getIconeURL());
        return badgeRepository.save(existingBadge);
    }

    @Override
    public void deleteBadge(Long id) {
        Badge badge = badgeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Badge non trouvé: " + id));
        // Avant de supprimer le badge, s'assurer qu'il n'est pas attribué ou gérer les attributions
        if (!utilisateurBadgeRepository.findByBadgeIdBadge(id).isEmpty()) {
            throw new IllegalStateException("Le badge ne peut être supprimé car il est attribué à des utilisateurs. Retirez d'abord les attributions.");
        }
        badgeRepository.delete(badge);
    }

    @Override
    public UtilisateurBadge assignBadgeToUtilisateur(Long idUtilisateur, Long idBadge) {
        if (utilisateurBadgeRepository.existsByUtilisateurIdUtilisateurAndBadgeIdBadge(idUtilisateur, idBadge)) {
            throw new IllegalStateException("Cet utilisateur possède déjà ce badge.");
        }
        Utilisateur utilisateur = utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé: " + idUtilisateur));
        Badge badge = badgeRepository.findById(idBadge)
                .orElseThrow(() -> new EntityNotFoundException("Badge non trouvé: " + idBadge));

        UtilisateurBadge utilisateurBadge = new UtilisateurBadge();
        utilisateurBadge.setUtilisateur(utilisateur);
        utilisateurBadge.setBadge(badge);
        utilisateurBadge.setDateObtention(LocalDateTime.now());
        return utilisateurBadgeRepository.save(utilisateurBadge);
    }

    @Override
    public void revokeBadgeFromUtilisateur(Long idUtilisateur, Long idBadge) {
        UtilisateurBadge utilisateurBadge = utilisateurBadgeRepository
                .findByUtilisateurIdUtilisateurAndBadgeIdBadge(idUtilisateur, idBadge)
                .orElseThrow(() -> new EntityNotFoundException("L'utilisateur " + idUtilisateur + " ne possède pas le badge " + idBadge + "."));
        utilisateurBadgeRepository.delete(utilisateurBadge);
    }

    @Override
    public List<UtilisateurBadge> getBadgesForUtilisateur(Long idUtilisateur) {
        if (!utilisateurRepository.existsById(idUtilisateur)) {
            throw new EntityNotFoundException("Utilisateur non trouvé: " + idUtilisateur);
        }
        return utilisateurBadgeRepository.findByUtilisateurIdUtilisateur(idUtilisateur);
    }

    @Override
    public List<UtilisateurBadge> getUtilisateursForBadge(Long idBadge) {
        if (!badgeRepository.existsById(idBadge)) {
            throw new EntityNotFoundException("Badge non trouvé: " + idBadge);
        }
        return utilisateurBadgeRepository.findByBadgeIdBadge(idBadge);
    }
}