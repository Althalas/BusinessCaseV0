package com.humanbooster.businesscase.service;


import com.humanbooster.businesscase.model.Message;
import java.util.List;
import java.util.Optional;

public interface MessageService {
    Message sendMessage(Message message, Long idEnvoyeur, Long idDestinataire, Long idReservationConcernee);
    Optional<Message> getMessageById(Long id);
    List<Message> getMessagesForUtilisateur(Long idUtilisateur); // Messages envoyés et reçus
    List<Message> getMessagesForReservation(Long idReservation);
    Message markAsRead(Long idMessage);
    // void deleteMessage(Long idMessage); // La suppression de messages peut être complexe (soft delete, etc.)
}