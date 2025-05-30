package com.humanbooster.businesscase.repository;


import com.humanbooster.businesscase.model.Borne;
import com.humanbooster.businesscase.model.EtatBorne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorneRepository extends JpaRepository<Borne, Long> {
    List<Borne> findByLieuRechargeIdLieu(Long idLieu);
    List<Borne> findByEtatBorne(EtatBorne etatBorne);
}