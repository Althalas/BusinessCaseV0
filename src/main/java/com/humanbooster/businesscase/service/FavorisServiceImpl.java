package com.humanbooster.businesscase.service;


import com.humanbooster.businesscase.model.Borne;
import com.humanbooster.businesscase.model.Favoris;
import com.humanbooster.businesscase.model.Utilisateur;
import com.humanbooster.businesscase.repository.BorneRepository;
import com.humanbooster.businesscase.repository.FavorisRepository;
import com.humanbooster.businesscase.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavorisServiceImpl implements FavorisService {

    private final FavorisRepository favorisRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final BorneRepository borneRepository;

    public FavorisServiceImpl(FavorisRepository favorisRepository,
                              UtilisateurRepository utilisateurRepository,
                              BorneRepository borneRepository) {
        this.favorisRepository = favorisRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.borneRepository = borneRepository;
    }

    @Override
    public Favoris addFavori(Long idUtilisateur, Long idBorne) {
        if (favorisRepository.existsByUtilisateurIdUtilisateurAndBorneIdBorne(idUtilisateur, idBorne)) {
            throw new IllegalStateException("Cette borne est déjà dans les favoris de l'utilisateur.");
        }
        Utilisateur utilisateur = utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé: " + idUtilisateur));
        Borne borne = borneRepository.findById(idBorne)
                .orElseThrow(() -> new EntityNotFoundException("Borne non trouvée: " + idBorne));

        Favoris favori = new Favoris();
        favori.setUtilisateur(utilisateur);
        favori.setBorne(borne);
        favori.setDateAjout(LocalDateTime.now());
        return favorisRepository.save(favori);
    }

    @Override
    public void removeFavori(Long idUtilisateur, Long idBorne) {
        Favoris favori = favorisRepository.findByUtilisateurIdUtilisateurAndBorneIdBorne(idUtilisateur, idBorne)
                .orElseThrow(() -> new EntityNotFoundException("Favori non trouvé pour cet utilisateur et cette borne."));
        favorisRepository.delete(favori);
    }

    @Override
    public List<Favoris> getFavorisByUtilisateur(Long idUtilisateur) {
        if (!utilisateurRepository.existsById(idUtilisateur)) {
            throw new EntityNotFoundException("Utilisateur non trouvé: " + idUtilisateur);
        }
        return favorisRepository.findByUtilisateurIdUtilisateur(idUtilisateur);
    }

    @Override
    public List<Borne> getBornesFavoritesByUtilisateur(Long idUtilisateur) {
        if (!utilisateurRepository.existsById(idUtilisateur)) {
            throw new EntityNotFoundException("Utilisateur non trouvé: " + idUtilisateur);
        }
        return favorisRepository.findByUtilisateurIdUtilisateur(idUtilisateur)
                .stream()
                .map(Favoris::getBorne)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isBorneInFavoris(Long idUtilisateur, Long idBorne) {
        return favorisRepository.existsByUtilisateurIdUtilisateurAndBorneIdBorne(idUtilisateur, idBorne);
    }
}