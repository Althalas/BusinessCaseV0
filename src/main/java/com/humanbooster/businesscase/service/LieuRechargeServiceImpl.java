package com.humanbooster.businesscase.service;


import com.humanbooster.businesscase.model.Adresse;
import com.humanbooster.businesscase.model.LieuRecharge;
import com.humanbooster.businesscase.model.Utilisateur;
import com.humanbooster.businesscase.repository.AdresseRepository;
import com.humanbooster.businesscase.repository.LieuRechargeRepository;
import com.humanbooster.businesscase.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LieuRechargeServiceImpl implements LieuRechargeService {

    private final LieuRechargeRepository lieuRechargeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final AdresseRepository adresseRepository;

    public LieuRechargeServiceImpl(LieuRechargeRepository lieuRechargeRepository,
                                   UtilisateurRepository utilisateurRepository,
                                   AdresseRepository adresseRepository) {
        this.lieuRechargeRepository = lieuRechargeRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.adresseRepository = adresseRepository;
    }

    @Override
    public LieuRecharge createLieuRecharge(LieuRecharge lieuRecharge, Long idProprietaire, Long idAdresse) {
        Utilisateur proprietaire = utilisateurRepository.findById(idProprietaire)
                .orElseThrow(() -> new EntityNotFoundException("Propriétaire non trouvé: " + idProprietaire));
        Adresse adresse = adresseRepository.findById(idAdresse)
                .orElseThrow(() -> new EntityNotFoundException("Adresse non trouvée: " + idAdresse));

        // Vérifier si l'adresse est déjà utilisée par un autre LieuRecharge (contrainte UNIQUE sur idAdresse dans LieuRecharge)
        // Cette vérification est normalement gérée par la contrainte de BD, mais peut être vérifiée ici aussi.
        // if (lieuRechargeRepository.existsByAdresseIdAdresse(idAdresse)) {
        //     throw new IllegalStateException("Cette adresse est déjà associée à un autre lieu de recharge.");
        // }

        lieuRecharge.setProprietaire(proprietaire);
        lieuRecharge.setAdresse(adresse);
        lieuRecharge.setIdLieu(null); // Assurer la création
        return lieuRechargeRepository.save(lieuRecharge);
    }

    @Override
    public Optional<LieuRecharge> getLieuRechargeById(Long id) {
        return lieuRechargeRepository.findById(id);
    }

    @Override
    public List<LieuRecharge> getAllLieuxRecharge() {
        return lieuRechargeRepository.findAll();
    }

    @Override
    public List<LieuRecharge> getLieuxRechargeByProprietaireId(Long idProprietaire) {
        if (!utilisateurRepository.existsById(idProprietaire)) {
            throw new EntityNotFoundException("Propriétaire non trouvé: " + idProprietaire);
        }
        return lieuRechargeRepository.findByProprietaireIdUtilisateur(idProprietaire);
    }

    @Override
    public LieuRecharge updateLieuRecharge(Long id, LieuRecharge lieuRechargeDetails, Long newIdProprietaire, Long newIdAdresse) {
        LieuRecharge existingLieu = lieuRechargeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LieuRecharge non trouvé: " + id));

        existingLieu.setNom(lieuRechargeDetails.getNom());
        existingLieu.setInstructionsAcces(lieuRechargeDetails.getInstructionsAcces());

        if (newIdProprietaire != null && (existingLieu.getProprietaire() == null || !newIdProprietaire.equals(existingLieu.getProprietaire().getIdUtilisateur()))) {
            Utilisateur proprietaire = utilisateurRepository.findById(newIdProprietaire)
                    .orElseThrow(() -> new EntityNotFoundException("Propriétaire non trouvé: " + newIdProprietaire));
            existingLieu.setProprietaire(proprietaire);
        }

        if (newIdAdresse != null && (existingLieu.getAdresse() == null || !newIdAdresse.equals(existingLieu.getAdresse().getIdAdresse()))) {
            Adresse adresse = adresseRepository.findById(newIdAdresse)
                    .orElseThrow(() -> new EntityNotFoundException("Adresse non trouvée: " + newIdAdresse));
            // Vérifier l'unicité de l'adresse si elle change pour un autre lieu.
            // Optional<LieuRecharge> conflitAdresse = lieuRechargeRepository.findByAdresseIdAdresseAndIdLieuNot(newIdAdresse, id);
            // if (conflitAdresse.isPresent()) {
            //     throw new IllegalStateException("Cette adresse est déjà associée à un autre lieu de recharge.");
            // }
            existingLieu.setAdresse(adresse);
        }
        return lieuRechargeRepository.save(existingLieu);
    }

    @Override
    public void deleteLieuRecharge(Long id) {
        LieuRecharge lieu = lieuRechargeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LieuRecharge non trouvé: " + id));
        if (lieu.getBornes() != null && !lieu.getBornes().isEmpty()) {
            throw new IllegalStateException("Le lieu ne peut être supprimé car il a des bornes associées.");
        }
        lieuRechargeRepository.deleteById(id);
    }
}