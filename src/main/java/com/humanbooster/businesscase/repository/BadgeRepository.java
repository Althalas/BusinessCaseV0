package com.humanbooster.businesscase.repository;


import com.humanbooster.businesscase.model.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    Optional<Badge> findByNomBadge(String nomBadge);
}