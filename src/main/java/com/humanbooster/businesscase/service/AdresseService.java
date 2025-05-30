package com.humanbooster.businesscase.service;


import com.humanbooster.businesscase.model.Adresse;
import java.util.List;
import java.util.Optional;

public interface AdresseService {
    Adresse save(Adresse adresse);
    Optional<Adresse> findById(Long id);
    List<Adresse> findAll();
    Adresse update(Long id, Adresse adresseDetails);
    void deleteById(Long id);
}