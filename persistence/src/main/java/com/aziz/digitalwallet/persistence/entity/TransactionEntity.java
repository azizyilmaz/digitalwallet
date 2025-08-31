package com.aziz.digitalwallet.persistence.entity;

import com.aziz.digitalwallet.domain.enums.OppositePartyType;
import com.aziz.digitalwallet.domain.enums.TransactionStatus;
import com.aziz.digitalwallet.domain.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
public class TransactionEntity {
    @Id
    private UUID id;

    private UUID walletId;
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    private OppositePartyType oppositePartyType;

    private String oppositeParty;

}
