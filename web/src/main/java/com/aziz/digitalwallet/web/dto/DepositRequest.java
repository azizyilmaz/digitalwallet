package com.aziz.digitalwallet.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class DepositRequest {
    @NotNull
    private UUID walletId;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private String source;

    @NotNull
    private String sourceType; // IBAN or PAYMENT
}
