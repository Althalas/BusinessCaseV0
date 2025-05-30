package com.humanbooster.businesscase.service;


import com.humanbooster.businesscase.model.Borne;
import com.humanbooster.businesscase.model.Media;
import com.humanbooster.businesscase.model.BorneMedia;

import java.util.List;
import java.util.Optional;

public interface MediaService {
    // Gestion des Media (métadonnées)
    Media createMedia(Media media); // La gestion du fichier réel est en dehors de cette simple création de métadonnées
    Optional<Media> getMediaById(Long id);
    List<Media> getAllMedia();
    void deleteMedia(Long id); // Devrait aussi gérer la suppression du fichier physique et des liens BorneMedia

    // Gestion des liens BorneMedia
    BorneMedia linkMediaToBorne(Long idBorne, Long idMedia, boolean estImagePrincipale);
    void unlinkMediaFromBorne(Long idBorne, Long idMedia);
    List<BorneMedia> getMediaForBorne(Long idBorne);
    Optional<BorneMedia> getBorneMediaLink(Long idBorne, Long idMedia);
    BorneMedia setMediaAsPrincipalForBorne(Long idBorne, Long idMedia);
}