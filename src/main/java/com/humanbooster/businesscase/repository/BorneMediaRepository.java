package com.humanbooster.businesscase.repository;


import com.humanbooster.businesscase.model.BorneMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorneMediaRepository extends JpaRepository<BorneMedia, Long> {
    List<BorneMedia> findByBorneIdBorne(Long idBorne);
    List<BorneMedia> findByMediaIdMedia(Long idMedia);
    Optional<BorneMedia> findByBorneIdBorneAndMediaIdMedia(Long idBorne, Long idMedia);
}