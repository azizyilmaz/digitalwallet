package com.aziz.digitalwallet.persistence.repository;

import com.aziz.digitalwallet.persistence.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataTokenRepository extends JpaRepository<TokenEntity, UUID> {

    Optional<TokenEntity> findByToken(String token);

    List<TokenEntity> findByActiveTrueAndCreatedAtBefore(Instant cutoff);

}