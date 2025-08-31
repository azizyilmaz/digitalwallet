package com.aziz.digitalwallet.persistence.repository;

import com.aziz.digitalwallet.persistence.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataWalletRepository extends JpaRepository<WalletEntity, UUID> {

    List<WalletEntity> findByCustomerId(UUID customerId);

}