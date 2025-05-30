package com.humanbooster.businesscase.model;

public enum RoleUtilisateur {
    CLIENT("Client"),
    PROPRIETAIRE("Proprietaire"),
    ADMINISTRATEUR("Administrateur");

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