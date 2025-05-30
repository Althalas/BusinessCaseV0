package com.humanbooster.businesscase.service;

import com.humanbooster.businesscase.model.Borne; // Changement ici
import java.util.List;
import java.util.Optional;

public interface BorneService {
    Borne createBorne(Borne borne, Long idLieu, Long idTypePrise);
    Optional<Borne> getBorneById(Long id);
    List<Borne> getAllBornes();
    List<Borne> getBornesByLieuId(Long idLieu);
    Borne updateBorne(Long id, Borne borneDetails, Long newIdLieu, Long newIdTypePrise);
    void deleteBorne(Long id);
}