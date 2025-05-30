package com.humanbooster.businesscase.repository;


import com.humanbooster.businesscase.model.Borne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorneRepository extends JpaRepository<Borne, Long> {
    List<Borne> findByLieuRechargeIdLieu(Long idLieu);
    List<Borne> findByEtatBorne(String etatBorne);
    // Vous pouvez ajouter des méthodes pour rechercher par GPS dans un rayon, etc.
}