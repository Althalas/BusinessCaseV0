package com.humanbooster.businesscase.service;


import com.humanbooster.businesscase.model.TypePrise;
import com.humanbooster.businesscase.repository.TypePriseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TypePriseServiceImpl implements TypePriseService {

    private final TypePriseRepository typePriseRepository;

    public TypePriseServiceImpl(TypePriseRepository typePriseRepository) {
        this.typePriseRepository = typePriseRepository;
    }

    @Override
    public TypePrise save(TypePrise typePrise) {
        typePrise.setIdTypePrise(null); // Assurer la création
        return typePriseRepository.save(typePrise);
    }

    @Override
    public Optional<TypePrise> findById(Long id) {
        return typePriseRepository.findById(id);
    }

    @Override
    public Optional<TypePrise> findByNom(String nom) {
        return typePriseRepository.findByNomTypePrise(nom);
    }

    @Override
    public List<TypePrise> findAll() {
        return typePriseRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        if (!typePriseRepository.existsById(id)) {
            throw new EntityNotFoundException("TypePrise non trouvé: " + id);
        }
        // Vérifier si ce type de prise est utilisé par des bornes
        // if (borneRepository.existsByTypePriseIdTypePrise(id)) {
        //    throw new IllegalStateException("Ce type de prise ne peut être supprimé car il est utilisé par des bornes.");
        // }
        typePriseRepository.deleteById(id);
    }
}