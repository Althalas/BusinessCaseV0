package com.humanbooster.businesscase.repository;


import com.humanbooster.businesscase.model.UtilisateurBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurBadgeRepository extends JpaRepository<UtilisateurBadge, Long> {
    List<UtilisateurBadge> findByUtilisateurIdUtilisateur(Long idUtilisateur);
    List<UtilisateurBadge> findByBadgeIdBadge(Long idBadge);
    Optional<UtilisateurBadge> findByUtilisateurIdUtilisateurAndBadgeIdBadge(Long idUtilisateur, Long idBadge);
    boolean existsByUtilisateurIdUtilisateurAndBadgeIdBadge(Long idUtilisateur, Long idBadge);
}