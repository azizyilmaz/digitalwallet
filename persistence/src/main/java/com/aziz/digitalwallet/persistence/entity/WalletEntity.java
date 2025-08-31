package com.aziz.digitalwallet.persistence.entity;

import com.aziz.digitalwallet.domain.enums.Currency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "wallets")
@Getter
@Setter
public class WalletEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID customerId;

    @Column(nullable = false)
    private String walletName;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private BigDecimal balance;
    private BigDecimal usableBalance;

    private boolean activeForShopping;
    private boolean activeForWithdraw;

}