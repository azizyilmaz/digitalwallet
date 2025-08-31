package com.aziz.digitalwallet.web.dto;

import com.aziz.digitalwallet.domain.enums.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateWalletRequest {

    @NotNull
    private UUID customerId;
    @NotBlank
    private String walletName;
    @NotNull
    private Currency currency;

}