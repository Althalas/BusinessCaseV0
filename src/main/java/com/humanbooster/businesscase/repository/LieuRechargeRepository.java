package com.humanbooster.businesscase.repository;

import com.humanbooster.businesscase.model.LieuRecharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LieuRechargeRepository extends JpaRepository<LieuRecharge, Long> {
    List<LieuRecharge> findByProprietaireIdUtilisateur(Long idProprietaire);
}