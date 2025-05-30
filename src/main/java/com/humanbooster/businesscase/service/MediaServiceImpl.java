package com.humanbooster.businesscase.service;


import com.humanbooster.businesscase.model.Borne;
import com.humanbooster.businesscase.model.Media;
import com.humanbooster.businesscase.model.BorneMedia;
import com.humanbooster.businesscase.repository.BorneRepository;
import com.humanbooster.businesscase.repository.MediaRepository;
import com.humanbooster.businesscase.repository.BorneMediaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final BorneRepository borneRepository;
    private final BorneMediaRepository borneMediaRepository;

    public MediaServiceImpl(MediaRepository mediaRepository,
                            BorneRepository borneRepository,
                            BorneMediaRepository borneMediaRepository) {
        this.mediaRepository = mediaRepository;
        this.borneRepository = borneRepository;
        this.borneMediaRepository = borneMediaRepository;
    }

    @Override
    public Media createMedia(Media media) {
        media.setIdMedia(null); // Assurer la création
        media.setDateUpload(LocalDateTime.now());
        // La logique de téléversement de fichier et de définition de l'URL
        // serait gérée ici ou avant d'appeler cette méthode.
        // Par exemple, media.setUrl("/uploads/media/" + media.getNomFichier());
        return mediaRepository.save(media);
    }

    @Override
    public Optional<Media> getMediaById(Long id) {
        return mediaRepository.findById(id);
    }

    @Override
    public List<Media> getAllMedia() {
        return mediaRepository.findAll();
    }

    @Override
    public void deleteMedia(Long id) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Media non trouvé: " + id));
        // Avant de supprimer le média, supprimer les liens
        borneMediaRepository.findByMediaIdMedia(id).forEach(borneMediaRepository::delete);
        // Ici, ajouter la logique pour supprimer le fichier physique du système de fichiers ou du cloud
        mediaRepository.delete(media);
    }

    @Override
    public BorneMedia linkMediaToBorne(Long idBorne, Long idMedia, boolean estImagePrincipale) {
        Borne borne = borneRepository.findById(idBorne)
                .orElseThrow(() -> new EntityNotFoundException("Borne non trouvée: " + idBorne));
        Media media = mediaRepository.findById(idMedia)
                .orElseThrow(() -> new EntityNotFoundException("Media non trouvé: " + idMedia));

        // Si le média doit être l'image principale, s'assurer qu'aucune autre n'est principale
        if (estImagePrincipale) {
            borneMediaRepository.findByBorneIdBorne(idBorne).forEach(bm -> {
                if (bm.isEstImagePrincipale()) {
                    bm.setEstImagePrincipale(false);
                    borneMediaRepository.save(bm);
                }
            });
        }

        // Vérifier si le lien existe déjà pour éviter les doublons, ou le mettre à jour.
        // Ici, on suppose une nouvelle création. Une contrainte unique sur (idBorne, idMedia) dans BorneMedia est recommandée.
        Optional<BorneMedia> existingLink = borneMediaRepository.findByBorneIdBorneAndMediaIdMedia(idBorne, idMedia);
        if (existingLink.isPresent()) {
            BorneMedia link = existingLink.get();
            link.setEstImagePrincipale(estImagePrincipale); // Mettre à jour si le lien existe déjà
            return borneMediaRepository.save(link);
        }


        BorneMedia borneMedia = new BorneMedia();
        borneMedia.setBorne(borne);
        borneMedia.setMedia(media);
        borneMedia.setEstImagePrincipale(estImagePrincipale);
        return borneMediaRepository.save(borneMedia);
    }

    @Override
    public void unlinkMediaFromBorne(Long idBorne, Long idMedia) {
        BorneMedia borneMedia = borneMediaRepository.findByBorneIdBorneAndMediaIdMedia(idBorne, idMedia)
                .orElseThrow(() -> new EntityNotFoundException("Lien entre Borne " + idBorne + " et Media " + idMedia + " non trouvé."));
        borneMediaRepository.delete(borneMedia);
    }

    @Override
    public List<BorneMedia> getMediaForBorne(Long idBorne) {
        if (!borneRepository.existsById(idBorne)) {
            throw new EntityNotFoundException("Borne non trouvée: " + idBorne);
        }
        return borneMediaRepository.findByBorneIdBorne(idBorne);
    }

    @Override
    public Optional<BorneMedia> getBorneMediaLink(Long idBorne, Long idMedia) {
        return borneMediaRepository.findByBorneIdBorneAndMediaIdMedia(idBorne, idMedia);
    }

    @Override
    public BorneMedia setMediaAsPrincipalForBorne(Long idBorne, Long idMedia) {
        BorneMedia linkToSetPrincipal = borneMediaRepository.findByBorneIdBorneAndMediaIdMedia(idBorne, idMedia)
                .orElseThrow(() -> new EntityNotFoundException("Lien BorneMedia non trouvé pour Borne " + idBorne + " et Media " + idMedia));

        // Mettre à false les autres images principales pour cette borne
        borneMediaRepository.findByBorneIdBorne(idBorne).forEach(bm -> {
            if (bm.isEstImagePrincipale() && !bm.equals(linkToSetPrincipal)) {
                bm.setEstImagePrincipale(false);
                borneMediaRepository.save(bm);
            }
        });

        linkToSetPrincipal.setEstImagePrincipale(true);
        return borneMediaRepository.save(linkToSetPrincipal);
    }
}