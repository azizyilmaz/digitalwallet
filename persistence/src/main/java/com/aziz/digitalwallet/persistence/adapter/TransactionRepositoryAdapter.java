package com.aziz.digitalwallet.persistence.adapter;

import com.aziz.digitalwallet.domain.model.Transaction;
import com.aziz.digitalwallet.persistence.entity.TransactionEntity;
import com.aziz.digitalwallet.persistence.repository.SpringDataTransactionRepository;
import com.aziz.digitalwallet.service.port.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TransactionRepositoryAdapter implements TransactionRepository {

    private final SpringDataTransactionRepository repo;

    @Override
    public Transaction save(Transaction tx) {
        TransactionEntity entity = toEntity(tx);
        TransactionEntity saved = repo.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        return repo.findById(id).map(this::toDomain);
    }

    @Override
    public List<Transaction> findByWalletId(UUID walletId) {
        return repo.findByWalletId(walletId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private TransactionEntity toEntity(Transaction tx) {
        TransactionEntity e = new TransactionEntity();
        e.setId(tx.getId());
        e.setWalletId(tx.getWalletId());
        e.setAmount(tx.getAmount());
        e.setType(tx.getType());
        e.setStatus(tx.getStatus());
        e.setOppositePartyType(tx.getOppositePartyType());
        e.setOppositeParty(tx.getOppositeParty());
        return e;
    }

    private Transaction toDomain(TransactionEntity e) {
        Transaction tx = new Transaction();
        tx.setId(e.getId());
        tx.setWalletId(e.getWalletId());
        tx.setAmount(e.getAmount());
        tx.setType(e.getType());
        tx.setStatus(e.getStatus());
        tx.setOppositePartyType(e.getOppositePartyType());
        tx.setOppositeParty(e.getOppositeParty());
        return tx;
    }
}
