package com.humanbooster.businesscase.service;


import com.humanbooster.businesscase.model.Vehicule;
import java.util.List;
import java.util.Optional;

public interface VehiculeService {
    Vehicule createVehicule(Vehicule vehicule, Long idProprietaire);
    Optional<Vehicule> getVehiculeById(Long id);
    List<Vehicule> getVehiculesByProprietaireId(Long idProprietaire);
    List<Vehicule> getAllVehicules(); // Pour admin potentiellement
    Vehicule updateVehicule(Long idVehicule, Vehicule vehiculeDetails, Long newIdProprietaire);
    void deleteVehicule(Long idVehicule);
}