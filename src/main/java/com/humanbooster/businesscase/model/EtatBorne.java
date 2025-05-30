package com.humanbooster.businesscase.model;


public enum EtatBorne {
    DISPONIBLE("Disponible"),
    EN_MAINTENANCE("En Maintenance"),
    HORS_SERVICE("Hors Service"),
    RESERVEE("Reservée"), // Lorsqu'une réservation acceptée est imminente ou en cours
    EN_UTILISATION("En Utilisation"); // Activement en charge

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