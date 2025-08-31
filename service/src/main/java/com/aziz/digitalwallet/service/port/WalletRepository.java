package com.aziz.digitalwallet.service.port;

import com.aziz.digitalwallet.domain.model.Wallet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepository {

    Wallet save(Wallet wallet);

    Optional<Wallet> findById(UUID id);

    List<Wallet> findByCustomerId(UUID customerId);

}