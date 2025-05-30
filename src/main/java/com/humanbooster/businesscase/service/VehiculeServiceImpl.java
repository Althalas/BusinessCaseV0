package com.humanbooster.businesscase.service;


import com.humanbooster.businesscase.model.Utilisateur;
import com.humanbooster.businesscase.model.Vehicule;
import com.humanbooster.businesscase.repository.UtilisateurRepository;
import com.humanbooster.businesscase.repository.VehiculeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VehiculeServiceImpl implements VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final UtilisateurRepository utilisateurRepository;

    public VehiculeServiceImpl(VehiculeRepository vehiculeRepository, UtilisateurRepository utilisateurRepository) {
        this.vehiculeRepository = vehiculeRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public Vehicule createVehicule(Vehicule vehicule, Long idProprietaire) {
        Utilisateur proprietaire = utilisateurRepository.findById(idProprietaire)
                .orElseThrow(() -> new EntityNotFoundException("Propriétaire non trouvé: " + idProprietaire));

        // Vérifier l'unicité de la plaque d'immatriculation
        vehiculeRepository.findByPlaqueImmatriculation(vehicule.getPlaqueImmatriculation()).ifPresent(v -> {
            throw new IllegalStateException("Un véhicule avec la plaque '" + vehicule.getPlaqueImmatriculation() + "' existe déjà.");
        });

        vehicule.setProprietaire(proprietaire);
        vehicule.setIdVehicule(null); // Assurer création
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public Optional<Vehicule> getVehiculeById(Long id) {
        return vehiculeRepository.findById(id);
    }

    @Override
    public List<Vehicule> getVehiculesByProprietaireId(Long idProprietaire) {
        if (!utilisateurRepository.existsById(idProprietaire)) {
            throw new EntityNotFoundException("Propriétaire non trouvé: " + idProprietaire);
        }
        return vehiculeRepository.findByProprietaireIdUtilisateur(idProprietaire);
    }

    @Override
    public List<Vehicule> getAllVehicules() {
        return vehiculeRepository.findAll();
    }

    @Override
    public Vehicule updateVehicule(Long idVehicule, Vehicule vehiculeDetails, Long newIdProprietaire) {
        Vehicule existingVehicule = vehiculeRepository.findById(idVehicule)
                .orElseThrow(() -> new EntityNotFoundException("Véhicule non trouvé: " + idVehicule));

        // Vérifier l'unicité de la plaque si elle change
        if (vehiculeDetails.getPlaqueImmatriculation() != null &&
                !vehiculeDetails.getPlaqueImmatriculation().equals(existingVehicule.getPlaqueImmatriculation())) {
            vehiculeRepository.findByPlaqueImmatriculation(vehiculeDetails.getPlaqueImmatriculation())
                    .ifPresent(v -> {
                        if (!v.getIdVehicule().equals(idVehicule)) { // Si c'est un autre véhicule
                            throw new IllegalStateException("Un autre véhicule avec la plaque '" + vehiculeDetails.getPlaqueImmatriculation() + "' existe déjà.");
                        }
                    });
        }

        existingVehicule.setPlaqueImmatriculation(vehiculeDetails.getPlaqueImmatriculation());
        existingVehicule.setMarque(vehiculeDetails.getMarque());
        existingVehicule.setModele(vehiculeDetails.getModele());
        existingVehicule.setAnnee(vehiculeDetails.getAnnee());
        existingVehicule.setCapaciteBatterieKWh(vehiculeDetails.getCapaciteBatterieKWh());

        if (newIdProprietaire != null &&
                (existingVehicule.getProprietaire() == null || !newIdProprietaire.equals(existingVehicule.getProprietaire().getIdUtilisateur()))) {
            Utilisateur proprietaire = utilisateurRepository.findById(newIdProprietaire)
                    .orElseThrow(() -> new EntityNotFoundException("Nouveau propriétaire non trouvé: " + newIdProprietaire));
            existingVehicule.setProprietaire(proprietaire);
        }

        return vehiculeRepository.save(existingVehicule);
    }

    @Override
    public void deleteVehicule(Long idVehicule) {
        if (!vehiculeRepository.existsById(idVehicule)) {
            throw new EntityNotFoundException("Véhicule non trouvé: " + idVehicule);
        }
        // Ajouter des vérifications si le véhicule est lié à d'autres éléments (ex: réservations par défaut)
        vehiculeRepository.deleteById(idVehicule);
    }
}