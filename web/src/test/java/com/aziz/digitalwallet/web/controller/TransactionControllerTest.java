package com.aziz.digitalwallet.web.controller;

import com.aziz.digitalwallet.common.exception.BusinessException;
import com.aziz.digitalwallet.common.exception.NotFoundException;
import com.aziz.digitalwallet.domain.enums.TransactionStatus;
import com.aziz.digitalwallet.domain.enums.TransactionType;
import com.aziz.digitalwallet.domain.model.Transaction;
import com.aziz.digitalwallet.service.application.TransactionService;
import com.aziz.digitalwallet.web.dto.DepositRequest;
import com.aziz.digitalwallet.web.dto.WithdrawRequest;
import com.aziz.digitalwallet.web.error.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@org.junit.jupiter.api.extension.ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private UUID walletId;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        walletId = UUID.randomUUID();
    }

    @Test
    void deposit_shouldReturnApprovedTransaction() throws Exception {
        Transaction tx = new Transaction();
        tx.setId(UUID.randomUUID());
        tx.setWalletId(walletId);
        tx.setAmount(BigDecimal.valueOf(500));
        tx.setType(TransactionType.DEPOSIT);
        tx.setStatus(TransactionStatus.APPROVED);

        when(transactionService.deposit(any(), any(), any(), any())).thenReturn(tx);

        DepositRequest req = new DepositRequest();
        req.setWalletId(walletId);
        req.setAmount(BigDecimal.valueOf(500));
        req.setSource("TR123");
        req.setSourceType("IBAN");

        mockMvc.perform(post("/api/transactions/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.type").value("DEPOSIT"));
    }

    @Test
    void withdraw_shouldReturnApprovedTransaction() throws Exception {
        Transaction tx = new Transaction();
        tx.setId(UUID.randomUUID());
        tx.setWalletId(walletId);
        tx.setAmount(BigDecimal.valueOf(200));
        tx.setType(TransactionType.WITHDRAW);
        tx.setStatus(TransactionStatus.APPROVED);

        when(transactionService.withdraw(any(), any(), any(), any())).thenReturn(tx);

        WithdrawRequest req = new WithdrawRequest();
        req.setWalletId(walletId);
        req.setAmount(BigDecimal.valueOf(200));
        req.setDestination("TR456");
        req.setDestinationType("IBAN");

        mockMvc.perform(post("/api/transactions/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.type").value("WITHDRAW"));
    }

    @Test
    void withdraw_shouldReturnErrorWhenInsufficientBalance() throws Exception {
        when(transactionService.withdraw(any(), any(), any(), any()))
                .thenThrow(new BusinessException("Insufficient balance"));

        WithdrawRequest req = new WithdrawRequest();
        req.setWalletId(walletId);
        req.setAmount(BigDecimal.valueOf(2000));
        req.setDestination("TR456");
        req.setDestinationType("IBAN");

        mockMvc.perform(post("/api/transactions/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BUSINESS_ERROR"))
                .andExpect(jsonPath("$.message").value("Insufficient balance"));
    }

    @Test
    void withdraw_shouldReturnErrorWhenWalletInactive() throws Exception {
        when(transactionService.withdraw(any(), any(), any(), any()))
                .thenThrow(new BusinessException("Withdraw not allowed for this wallet"));

        WithdrawRequest req = new WithdrawRequest();
        req.setWalletId(walletId);
        req.setAmount(BigDecimal.valueOf(50));
        req.setDestination("TR456");
        req.setDestinationType("IBAN");

        mockMvc.perform(post("/api/transactions/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BUSINESS_ERROR"))
                .andExpect(jsonPath("$.message").value("Withdraw not allowed for this wallet"));
    }

    @Test
    void deposit_shouldReturnErrorWhenWalletNotFound() throws Exception {
        when(transactionService.deposit(any(), any(), any(), any()))
                .thenThrow(new NotFoundException("Wallet not found"));

        DepositRequest req = new DepositRequest();
        req.setWalletId(walletId);
        req.setAmount(BigDecimal.valueOf(100));
        req.setSource("TR123");
        req.setSourceType("IBAN");

        mockMvc.perform(post("/api/transactions/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Wallet not found"));
    }
}
