package com.aziz.digitalwallet.domain.model;

import com.aziz.digitalwallet.domain.enums.OppositePartyType;
import com.aziz.digitalwallet.domain.enums.TransactionStatus;
import com.aziz.digitalwallet.domain.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class Transaction {

    private UUID id;
    private UUID walletId;
    private BigDecimal amount;
    private TransactionType type;
    private TransactionStatus status;
    private OppositePartyType oppositePartyType;
    private String oppositeParty;

}
