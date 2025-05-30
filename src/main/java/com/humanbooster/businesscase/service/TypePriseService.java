package com.humanbooster.businesscase.service;


import com.humanbooster.businesscase.model.TypePrise;
import java.util.List;
import java.util.Optional;

public interface TypePriseService {
    TypePrise save(TypePrise typePrise);
    Optional<TypePrise> findById(Long id);
    Optional<TypePrise> findByNom(String nom);
    List<TypePrise> findAll();
    void deleteById(Long id);
}