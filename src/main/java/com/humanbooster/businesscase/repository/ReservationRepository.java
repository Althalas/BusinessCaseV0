package com.humanbooster.businesscase.repository;


import com.humanbooster.businesscase.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByClientIdUtilisateur(Long idUtilisateurClient);
    List<Reservation> findByBorneIdBorne(Long idBorne);
    List<Reservation> findByBorneIdBorneAndDateHeureFinAfterAndDateHeureDebutBefore(
            Long idBorne, LocalDateTime dateDebutPlage, LocalDateTime dateFinPlage
    );
}