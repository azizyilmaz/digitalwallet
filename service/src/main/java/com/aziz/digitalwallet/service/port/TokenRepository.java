package com.aziz.digitalwallet.service.port;

import com.aziz.digitalwallet.domain.model.Token;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TokenRepository {

    Token save(Token token);

    Optional<Token> findByToken(String token);

    List<Token> findByActiveTrueAndCreatedAtBefore(Instant cutoff);
}
