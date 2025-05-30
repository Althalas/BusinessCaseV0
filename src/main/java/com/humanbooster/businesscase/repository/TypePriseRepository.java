package com.humanbooster.businesscase.repository;


import com.humanbooster.businesscase.model.TypePrise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TypePriseRepository extends JpaRepository<TypePrise, Long> {
    Optional<TypePrise> findByNomTypePrise(String nomTypePrise);
}