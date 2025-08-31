package com.aziz.digitalwallet.service.port;

import com.aziz.digitalwallet.domain.model.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {

    Transaction save(Transaction tx);

    Optional<Transaction> findById(UUID id);

    List<Transaction> findByWalletId(UUID walletId);

}
