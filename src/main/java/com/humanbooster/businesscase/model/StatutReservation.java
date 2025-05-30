package com.humanbooster.businesscase.model;

public enum StatutReservation {
    EN_ATTENTE_CONFIRMATION("EnAttenteConfirmation"), // Nouvelle demande, en attente d'acceptation par le propriétaire
    ACCEPTEE("Acceptee"), // Réservation acceptée par le propriétaire
    REFUSEE("Refusee"), // Réservation refusée par le propriétaire
    ANNULEE_CLIENT("AnnuleeClient"), // Réservation annulée par le client
    ANNULEE_PROPRIETAIRE("AnnuleeProprietaire"), // Réservation annulée par le propriétaire (après acceptation)
    EN_COURS_UTILISATION("EnCoursUtilisation"), // Le client utilise la borne
    TERMINEE("Terminee"), // La période de réservation est terminée
    PAYEE("Payee"), // La réservation est terminée et payée
    PROBLEME_SIGNALE("ProblemeSignalé"); // Un problème a été signalé pour cette réservation

    private final String libelle;

    StatutReservation(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
}