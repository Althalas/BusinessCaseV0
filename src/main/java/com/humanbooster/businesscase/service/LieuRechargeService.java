package com.humanbooster.businesscase.service;


import com.humanbooster.businesscase.model.LieuRecharge;
import java.util.List;
import java.util.Optional;

public interface LieuRechargeService {
    LieuRecharge createLieuRecharge(LieuRecharge lieuRecharge, Long idProprietaire, Long idAdresse);
    Optional<LieuRecharge> getLieuRechargeById(Long id);
    List<LieuRecharge> getAllLieuxRecharge();
    List<LieuRecharge> getLieuxRechargeByProprietaireId(Long idProprietaire);
    LieuRecharge updateLieuRecharge(Long id, LieuRecharge lieuRechargeDetails, Long newIdProprietaire, Long newIdAdresse);
    void deleteLieuRecharge(Long id);
}