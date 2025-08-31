package com.aziz.digitalwallet.web.controller;

import com.aziz.digitalwallet.domain.model.Wallet;
import com.aziz.digitalwallet.service.application.WalletService;
import com.aziz.digitalwallet.web.dto.CreateWalletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<Wallet> createWallet(@Valid @RequestBody CreateWalletRequest request) {
        Wallet wallet = walletService.createWallet(request.getCustomerId(), request.getWalletName(), request.getCurrency());
        return ResponseEntity.ok(wallet);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Wallet>> listWallets(@PathVariable UUID customerId) {
        return ResponseEntity.ok(walletService.getWalletsByCustomer(customerId));
    }

}