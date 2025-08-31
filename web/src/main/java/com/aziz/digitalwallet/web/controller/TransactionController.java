package com.aziz.digitalwallet.web.controller;

import com.aziz.digitalwallet.domain.model.Transaction;
import com.aziz.digitalwallet.service.application.TransactionService;
import com.aziz.digitalwallet.web.dto.DepositRequest;
import com.aziz.digitalwallet.web.dto.WithdrawRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@Valid @RequestBody DepositRequest request) {
        return ResponseEntity.ok(transactionService.deposit(
                request.getWalletId(),
                request.getAmount(),
                request.getSource(),
                request.getSourceType()
        ));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@Valid @RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(transactionService.withdraw(
                request.getWalletId(),
                request.getAmount(),
                request.getDestination(),
                request.getDestinationType()
        ));
    }

    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<List<Transaction>> list(@PathVariable UUID walletId) {
        return ResponseEntity.ok(transactionService.listTransactions(walletId));
    }
}
