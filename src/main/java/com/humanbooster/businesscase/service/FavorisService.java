package com.humanbooster.businesscase.service;


import com.humanbooster.businesscase.model.Borne;
import com.humanbooster.businesscase.model.Favoris;
import com.humanbooster.businesscase.model.Utilisateur;

import java.util.List;

public interface FavorisService {
    Favoris addFavori(Long idUtilisateur, Long idBorne);
    void removeFavori(Long idUtilisateur, Long idBorne);
    List<Favoris> getFavorisByUtilisateur(Long idUtilisateur); // Retourne les objets Favoris
    List<Borne> getBornesFavoritesByUtilisateur(Long idUtilisateur); // Retourne directement les Bornes
    boolean isBorneInFavoris(Long idUtilisateur, Long idBorne);
}