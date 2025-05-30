package com.humanbooster.businesscase.model;


public enum EtatBorne {
    DISPONIBLE("Disponible"),
    EN_MAINTENANCE("En Maintenance"),
    HORS_SERVICE("Hors Service"),
    RESERVEE("Reservée"),
    EN_UTILISATION("En Utilisation");

    private final String libelle;

    EtatBorne(String libelle) {
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