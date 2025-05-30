package com.humanbooster.businesscase.repository;

import com.humanbooster.businesscase.model.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {
    List<Vehicule> findByProprietaireIdUtilisateur(Long idUtilisateur);
    Optional<Vehicule> findByPlaqueImmatriculation(String plaqueImmatriculation);
}