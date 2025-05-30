package com.humanbooster.businesscase.service;


import com.humanbooster.businesscase.model.Message;
import com.humanbooster.businesscase.model.Reservation;
import com.humanbooster.businesscase.model.Utilisateur;
import com.humanbooster.businesscase.repository.MessageRepository;
import com.humanbooster.businesscase.repository.ReservationRepository;
import com.humanbooster.businesscase.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ReservationRepository reservationRepository;

    public MessageServiceImpl(MessageRepository messageRepository,
                              UtilisateurRepository utilisateurRepository,
                              ReservationRepository reservationRepository) {
        this.messageRepository = messageRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Message sendMessage(Message message, Long idEnvoyeur, Long idDestinataire, Long idReservationConcernee) {
        Utilisateur envoyeur = utilisateurRepository.findById(idEnvoyeur)
                .orElseThrow(() -> new EntityNotFoundException("Envoyeur non trouvé: " + idEnvoyeur));
        message.setEnvoyeur(envoyeur);

        if (idDestinataire != null) {
            Utilisateur destinataire = utilisateurRepository.findById(idDestinataire)
                    .orElseThrow(() -> new EntityNotFoundException("Destinataire non trouvé: " + idDestinataire));
            message.setDestinataire(destinataire);
        } else {
            message.setDestinataire(null); // Message système ou sans destinataire direct
        }

        if (idReservationConcernee != null) {
            Reservation reservation = reservationRepository.findById(idReservationConcernee)
                    .orElseThrow(() -> new EntityNotFoundException("Réservation non trouvée: " + idReservationConcernee));
            message.setReservationConcernee(reservation);
        } else {
            message.setReservationConcernee(null);
        }

        message.setIdMessage(null); // Assurer la création
        message.setDateEnvoi(LocalDateTime.now());
        message.setEstLu(false);
        return messageRepository.save(message);
    }

    @Override
    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> getMessagesForUtilisateur(Long idUtilisateur) {
        if (!utilisateurRepository.existsById(idUtilisateur)) {
            throw new EntityNotFoundException("Utilisateur non trouvé: " + idUtilisateur);
        }
        // Récupère les messages où l'utilisateur est soit l'envoyeur, soit le destinataire
        return messageRepository.findByDestinataireIdUtilisateurOrEnvoyeurIdUtilisateurOrderByDateEnvoiDesc(idUtilisateur, idUtilisateur);
    }

    @Override
    public List<Message> getMessagesForReservation(Long idReservation) {
        if (!reservationRepository.existsById(idReservation)) {
            throw new EntityNotFoundException("Réservation non trouvée: " + idReservation);
        }
        return messageRepository.findByReservationConcerneeIdReservation(idReservation);
    }

    @Override
    public Message markAsRead(Long idMessage) {
        Message message = messageRepository.findById(idMessage)
                .orElseThrow(() -> new EntityNotFoundException("Message non trouvé: " + idMessage));
        message.setEstLu(true);
        return messageRepository.save(message);
    }
}