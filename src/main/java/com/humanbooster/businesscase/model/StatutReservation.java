package com.humanbooster.businesscase.model;

public enum StatutReservation {
    EN_ATTENTE_CONFIRMATION("EnAttenteConfirmation"),
    ACCEPTEE("Acceptee"),
    REFUSEE("Refusee"),
    ANNULEE_CLIENT("AnnuleeClient"),
    ANNULEE_PROPRIETAIRE("AnnuleeProprietaire"),
    EN_COURS_UTILISATION("EnCoursUtilisation"),
    TERMINEE("Terminee"),
    PAYEE("Payee"),
    PROBLEME_SIGNALE("ProblemeSignalé");

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