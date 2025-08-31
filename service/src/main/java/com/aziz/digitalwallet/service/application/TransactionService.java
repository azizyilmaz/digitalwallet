package com.aziz.digitalwallet.service.application;

import com.aziz.digitalwallet.common.exception.BusinessException;
import com.aziz.digitalwallet.common.exception.NotFoundException;
import com.aziz.digitalwallet.domain.enums.TransactionStatus;
import com.aziz.digitalwallet.domain.enums.TransactionType;
import com.aziz.digitalwallet.domain.model.Transaction;
import com.aziz.digitalwallet.domain.model.Wallet;
import com.aziz.digitalwallet.service.port.TransactionRepository;
import com.aziz.digitalwallet.service.port.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    @Transactional
    public Transaction deposit(UUID walletId, BigDecimal amount, String source, String sourceType) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new NotFoundException("Wallet not found: " + walletId));

        Transaction tx = new Transaction();
        tx.setId(UUID.randomUUID());
        tx.setWalletId(walletId);
        tx.setAmount(amount);
        tx.setType(TransactionType.DEPOSIT);
        tx.setOppositeParty(source);
        tx.setOppositePartyType("IBAN".equalsIgnoreCase(sourceType) ? com.aziz.digitalwallet.domain.enums.OppositePartyType.IBAN : com.aziz.digitalwallet.domain.enums.OppositePartyType.PAYMENT);

        if (amount.compareTo(BigDecimal.valueOf(1000)) > 0) {
            tx.setStatus(TransactionStatus.PENDING);
            wallet.setBalance(wallet.getBalance().add(amount));
        } else {
            tx.setStatus(TransactionStatus.APPROVED);
            wallet.setBalance(wallet.getBalance().add(amount));
            wallet.setUsableBalance(wallet.getUsableBalance().add(amount));
        }

        walletRepository.save(wallet);
        return transactionRepository.save(tx);
    }

    @Transactional
    public Transaction withdraw(UUID walletId, BigDecimal amount, String destination, String destinationType) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new NotFoundException("Wallet not found: " + walletId));

        if (!wallet.isActiveForWithdraw()) {
            throw new BusinessException("Withdraw not allowed for this wallet");
        }

        Transaction tx = new Transaction();
        tx.setId(UUID.randomUUID());
        tx.setWalletId(walletId);
        tx.setAmount(amount);
        tx.setType(TransactionType.WITHDRAW);
        tx.setOppositeParty(destination);
        tx.setOppositePartyType("IBAN".equalsIgnoreCase(destinationType) ? com.aziz.digitalwallet.domain.enums.OppositePartyType.IBAN : com.aziz.digitalwallet.domain.enums.OppositePartyType.PAYMENT);

        if (amount.compareTo(BigDecimal.valueOf(1000)) > 0) {
            tx.setStatus(TransactionStatus.PENDING);
            wallet.setUsableBalance(wallet.getUsableBalance().subtract(amount));
        } else {
            if (wallet.getUsableBalance().compareTo(amount) < 0) {
                throw new BusinessException("Insufficient balance");
            }
            tx.setStatus(TransactionStatus.APPROVED);
            wallet.setBalance(wallet.getBalance().subtract(amount));
            wallet.setUsableBalance(wallet.getUsableBalance().subtract(amount));
        }

        walletRepository.save(wallet);
        return transactionRepository.save(tx);
    }

    public List<Transaction> listTransactions(UUID walletId) {
        return transactionRepository.findByWalletId(walletId);
    }
}