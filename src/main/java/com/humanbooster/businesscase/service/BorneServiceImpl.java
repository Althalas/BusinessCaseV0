package com.humanbooster.businesscase.service;


import com.humanbooster.businesscase.model.Borne;
import com.humanbooster.businesscase.model.LieuRecharge;
import com.humanbooster.businesscase.model.TypePrise;
import com.humanbooster.businesscase.repository.BorneRepository;
import com.humanbooster.businesscase.repository.LieuRechargeRepository;
import com.humanbooster.businesscase.repository.TypePriseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BorneServiceImpl implements BorneService {

    private final BorneRepository borneRepository;
    private final LieuRechargeRepository lieuRechargeRepository;
    private final TypePriseRepository typePriseRepository;

    public BorneServiceImpl(BorneRepository borneRepository,
                            LieuRechargeRepository lieuRechargeRepository,
                            TypePriseRepository typePriseRepository) {
        this.borneRepository = borneRepository;
        this.lieuRechargeRepository = lieuRechargeRepository;
        this.typePriseRepository = typePriseRepository;
    }

    @Override
    public Borne createBorne(Borne borne, Long idLieu, Long idTypePrise) {
        LieuRecharge lieu = lieuRechargeRepository.findById(idLieu)
                .orElseThrow(() -> new EntityNotFoundException("LieuRecharge non trouvé avec l'id: " + idLieu));
        TypePrise typePrise = typePriseRepository.findById(idTypePrise)
                .orElseThrow(() -> new EntityNotFoundException("TypePrise non trouvé avec l'id: " + idTypePrise));

        borne.setLieuRecharge(lieu);
        borne.setTypePrise(typePrise);
        // L'ID sera généré par la base de données, donc borne.getIdBorne() sera null ici
        return borneRepository.save(borne);
    }

    @Override
    public Optional<Borne> getBorneById(Long id) {
        return borneRepository.findById(id);
    }

    @Override
    public List<Borne> getAllBornes() {
        return borneRepository.findAll();
    }

    @Override
    public List<Borne> getBornesByLieuId(Long idLieu) {
        if (!lieuRechargeRepository.existsById(idLieu)) {
            throw new EntityNotFoundException("LieuRecharge non trouvé avec l'id: " + idLieu);
        }
        return borneRepository.findByLieuRechargeIdLieu(idLieu);
    }

    @Override
    public Borne updateBorne(Long id, Borne borneDetails, Long newIdLieu, Long newIdTypePrise) {
        Borne existingBorne = borneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Borne non trouvée avec l'id: " + id));

        existingBorne.setNom(borneDetails.getNom());
        existingBorne.setGpsLatitude(borneDetails.getGpsLatitude());
        existingBorne.setGpsLongitude(borneDetails.getGpsLongitude());
        existingBorne.setInstructionsUtilisation(borneDetails.getInstructionsUtilisation());
        existingBorne.setPuissanceKW(borneDetails.getPuissanceKW());
        existingBorne.setEstMurale(borneDetails.getEstMurale());
        existingBorne.setEtatBorne(borneDetails.getEtatBorne());
        existingBorne.setTarifHoraire(borneDetails.getTarifHoraire());

        if (newIdLieu != null && (existingBorne.getLieuRecharge() == null || !newIdLieu.equals(existingBorne.getLieuRecharge().getIdLieu()))) {
            LieuRecharge lieu = lieuRechargeRepository.findById(newIdLieu)
                    .orElseThrow(() -> new EntityNotFoundException("LieuRecharge non trouvé avec l'id: " + newIdLieu));
            existingBorne.setLieuRecharge(lieu);
        }

        if (newIdTypePrise != null && (existingBorne.getTypePrise() == null || !newIdTypePrise.equals(existingBorne.getTypePrise().getIdTypePrise()))) {
            TypePrise typePrise = typePriseRepository.findById(newIdTypePrise)
                    .orElseThrow(() -> new EntityNotFoundException("TypePrise non trouvé avec l'id: " + newIdTypePrise));
            existingBorne.setTypePrise(typePrise);
        }

        return borneRepository.save(existingBorne);
    }

    @Override
    public void deleteBorne(Long id) {
        Borne borne = borneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Borne non trouvée avec l'id: " + id));
        if (borne.getReservations() != null && !borne.getReservations().isEmpty()) {
            throw new IllegalStateException("La borne ne peut pas être supprimée car elle a des réservations associées. [cite: 6]");
        }
        borneRepository.deleteById(id);
    }
}
