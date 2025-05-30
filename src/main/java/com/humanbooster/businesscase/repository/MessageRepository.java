package com.humanbooster.businesscase.repository;


import com.humanbooster.businesscase.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByEnvoyeurIdUtilisateur(Long idEnvoyeur);
    List<Message> findByDestinataireIdUtilisateur(Long idDestinataire);
    List<Message> findByReservationConcerneeIdReservation(Long idReservation);
    // Pour une "boîte de réception" d'un utilisateur
    List<Message> findByDestinataireIdUtilisateurOrEnvoyeurIdUtilisateurOrderByDateEnvoiDesc(Long idDestinataire, Long idEnvoyeur);
}