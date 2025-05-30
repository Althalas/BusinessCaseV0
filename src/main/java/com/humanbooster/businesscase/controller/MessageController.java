package com.humanbooster.businesscase.controller;


import com.humanbooster.businesscase.model.Message;
import com.humanbooster.businesscase.service.MessageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // Pour le corps de la requête de création de message

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // Envoyer un message. Le corps de la requête contiendra le contenu,
    // et les IDs de l'envoyeur, destinataire (optionnel), et réservation (optionnel)
    // seront passés comme paramètres ou dans un objet de requête plus structuré.
    // Ici, nous utilisons @RequestBody pour le message (principalement le contenu)
    // et @RequestParam pour les IDs.
    @PostMapping
    public ResponseEntity<?> sendMessage(@Valid @RequestBody Message messagePartial,
                                         @RequestParam Long idEnvoyeur,
                                         @RequestParam(required = false) Long idDestinataire,
                                         @RequestParam(required = false) Long idReservationConcernee) {
        try {
            // L'objet messagePartial ne contiendra que les champs que le client envoie, comme "contenu".
            // Le service se chargera de lier les entités.
            Message messageComplet = new Message();
            messageComplet.setContenu(messagePartial.getContenu());

            Message sentMessage = messageService.sendMessage(messageComplet, idEnvoyeur, idDestinataire, idReservationConcernee);
            return new ResponseEntity<>(sentMessage, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{idMessage}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long idMessage) {
        return messageService.getMessageById(idMessage)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/utilisateur/{idUtilisateur}")
    public ResponseEntity<?> getMessagesForUtilisateur(@PathVariable Long idUtilisateur) {
        try {
            List<Message> messages = messageService.getMessagesForUtilisateur(idUtilisateur);
            return ResponseEntity.ok(messages);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/reservation/{idReservation}")
    public ResponseEntity<?> getMessagesForReservation(@PathVariable Long idReservation) {
        try {
            List<Message> messages = messageService.getMessagesForReservation(idReservation);
            return ResponseEntity.ok(messages);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{idMessage}/mark-as-read")
    public ResponseEntity<?> markMessageAsRead(@PathVariable Long idMessage) {
        try {
            Message message = messageService.markAsRead(idMessage);
            return ResponseEntity.ok(message);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}