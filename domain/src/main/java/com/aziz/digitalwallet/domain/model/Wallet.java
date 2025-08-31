package com.aziz.digitalwallet.domain.model;

import com.aziz.digitalwallet.domain.enums.Currency;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class Wallet {

    private UUID id;
    private UUID customerId;
    private String walletName;
    private Currency currency;
    private BigDecimal balance;
    private BigDecimal usableBalance;
    private boolean activeForShopping;
    private boolean activeForWithdraw;

}
