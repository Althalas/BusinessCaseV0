package com.humanbooster.businesscase.repository;

import com.humanbooster.businesscase.model.Favoris;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavorisRepository extends JpaRepository<Favoris, Long> {
    List<Favoris> findByUtilisateurIdUtilisateur(Long idUtilisateur);
    List<Favoris> findByBorneIdBorne(Long idBorne);
    Optional<Favoris> findByUtilisateurIdUtilisateurAndBorneIdBorne(Long idUtilisateur, Long idBorne);
    boolean existsByUtilisateurIdUtilisateurAndBorneIdBorne(Long idUtilisateur, Long idBorne);
}