package com.aziz.digitalwallet.service.application;

import com.aziz.digitalwallet.common.exception.BusinessException;
import com.aziz.digitalwallet.common.exception.NotFoundException;
import com.aziz.digitalwallet.domain.enums.TransactionStatus;
import com.aziz.digitalwallet.domain.enums.TransactionType;
import com.aziz.digitalwallet.domain.model.Transaction;
import com.aziz.digitalwallet.domain.model.Wallet;
import com.aziz.digitalwallet.service.port.TransactionRepository;
import com.aziz.digitalwallet.service.port.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    private TransactionRepository transactionRepository;
    private WalletRepository walletRepository;
    private TransactionService transactionService;

    private UUID walletId;
    private Wallet wallet;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        walletRepository = mock(WalletRepository.class);
        transactionService = new TransactionService(transactionRepository, walletRepository);

        walletId = UUID.randomUUID();
        wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(BigDecimal.valueOf(1000));
        wallet.setUsableBalance(BigDecimal.valueOf(1000));
        wallet.setActiveForWithdraw(true);
    }

    @Test
    void deposit_shouldApproveWhenAmountIsSmall() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        Transaction tx = transactionService.deposit(walletId, BigDecimal.valueOf(500), "TR123", "IBAN");

        assertThat(tx.getType()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(tx.getStatus()).isEqualTo(TransactionStatus.APPROVED);
        assertThat(wallet.getBalance()).isEqualByComparingTo("1500");
        assertThat(wallet.getUsableBalance()).isEqualByComparingTo("1500");
    }

    @Test
    void deposit_shouldBePendingWhenAmountIsLarge() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        Transaction tx = transactionService.deposit(walletId, BigDecimal.valueOf(2000), "TR123", "IBAN");

        assertThat(tx.getStatus()).isEqualTo(TransactionStatus.PENDING);
        assertThat(wallet.getBalance()).isEqualByComparingTo("3000");
        assertThat(wallet.getUsableBalance()).isEqualByComparingTo("1000"); // değişmedi
    }

    @Test
    void withdraw_shouldApproveWhenEnoughBalanceAndSmallAmount() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        Transaction tx = transactionService.withdraw(walletId, BigDecimal.valueOf(500), "TR456", "IBAN");

        assertThat(tx.getType()).isEqualTo(TransactionType.WITHDRAW);
        assertThat(tx.getStatus()).isEqualTo(TransactionStatus.APPROVED);
        assertThat(wallet.getBalance()).isEqualByComparingTo("500");
        assertThat(wallet.getUsableBalance()).isEqualByComparingTo("500");
    }

    @Test
    void withdraw_shouldBePendingWhenLargeAmount() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        Transaction tx = transactionService.withdraw(walletId, BigDecimal.valueOf(2000), "TR456", "IBAN");

        assertThat(tx.getStatus()).isEqualTo(TransactionStatus.PENDING);
        assertThat(wallet.getUsableBalance()).isEqualByComparingTo("-1000"); // şimdilik eksiye düşebilir
    }

    @Test
    void withdraw_shouldThrowWhenInsufficientBalance() {
        wallet.setUsableBalance(BigDecimal.valueOf(100));
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        assertThatThrownBy(() -> transactionService.withdraw(walletId, BigDecimal.valueOf(200), "TR456", "IBAN"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Insufficient balance");
    }

    @Test
    void withdraw_shouldThrowWhenWalletInactive() {
        wallet.setActiveForWithdraw(false);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        assertThatThrownBy(() -> transactionService.withdraw(walletId, BigDecimal.valueOf(50), "TR456", "IBAN"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Withdraw not allowed");
    }

    @Test
    void deposit_shouldThrowWhenWalletNotFound() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.deposit(walletId, BigDecimal.valueOf(100), "TR123", "IBAN"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Wallet not found");
    }

    @Test
    void withdraw_shouldThrowWhenWalletNotFound() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.withdraw(walletId, BigDecimal.valueOf(100), "TR456", "IBAN"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Wallet not found");
    }
}
