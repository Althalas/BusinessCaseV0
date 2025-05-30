package com.humanbooster.businesscase.model;

public enum RoleUtilisateur {
    CLIENT("Client"), // Utilisateur qui réserve des bornes
    PROPRIETAIRE("Proprietaire"), // Utilisateur qui met à disposition des bornes
    ADMINISTRATEUR("Administrateur"); // Administrateur du système

    private final String libelle;

    RoleUtilisateur(String libelle) {
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