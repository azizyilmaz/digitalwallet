package com.aziz.digitalwallet.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class Token {

    private UUID id;
    private String token; // JWT
    private Instant createdAt;
    private Instant expiresAt;
    private boolean active;
    private User user;
}
