package com.humanbooster.businesscase.service;


import com.humanbooster.businesscase.model.Adresse;
import com.humanbooster.businesscase.repository.AdresseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdresseServiceImpl implements AdresseService {

    private final AdresseRepository adresseRepository;

    public AdresseServiceImpl(AdresseRepository adresseRepository) {
        this.adresseRepository = adresseRepository;
    }

    @Override
    public Adresse save(Adresse adresse) {
        adresse.setIdAdresse(null); // Assurer la création
        return adresseRepository.save(adresse);
    }

    @Override
    public Optional<Adresse> findById(Long id) {
        return adresseRepository.findById(id);
    }

    @Override
    public List<Adresse> findAll() {
        return adresseRepository.findAll();
    }

    @Override
    public Adresse update(Long id, Adresse adresseDetails) {
        Adresse existingAdresse = adresseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Adresse non trouvée: " + id));

        existingAdresse.setNumeroRue(adresseDetails.getNumeroRue());
        existingAdresse.setRue(adresseDetails.getRue());
        existingAdresse.setComplement(adresseDetails.getComplement());
        existingAdresse.setEtage(adresseDetails.getEtage());
        existingAdresse.setCodePostal(adresseDetails.getCodePostal());
        existingAdresse.setVille(adresseDetails.getVille());
        existingAdresse.setRegion(adresseDetails.getRegion());
        existingAdresse.setPays(adresseDetails.getPays());
        existingAdresse.setLatitude(adresseDetails.getLatitude());
        existingAdresse.setLongitude(adresseDetails.getLongitude());

        return adresseRepository.save(existingAdresse);
    }

    @Override
    public void deleteById(Long id) {
        if (!adresseRepository.existsById(id)) {
            throw new EntityNotFoundException("Adresse non trouvée: " + id);
        }
        // Vérifier les dépendances (Utilisateur, LieuRecharge) avant suppression
        // if (utilisateurRepository.existsByAdresseIdAdresse(id) || lieuRechargeRepository.existsByAdresseIdAdresse(id)) {
        //    throw new IllegalStateException("L'adresse ne peut être supprimée car elle est utilisée.");
        // }
        adresseRepository.deleteById(id);
    }
}