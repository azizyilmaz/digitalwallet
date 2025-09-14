package com.aziz.digitalwallet.service.application;

import com.aziz.digitalwallet.common.exception.NotFoundException;
import com.aziz.digitalwallet.domain.model.Token;
import com.aziz.digitalwallet.domain.model.User;
import com.aziz.digitalwallet.service.component.JwtProvider;
import com.aziz.digitalwallet.service.port.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public Token createTokenForUser(User user) {
        UUID id = UUID.randomUUID();
        Set<String> roles = user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toSet());
        String jwt = jwtProvider.createToken(user.getId(), roles);

        Token token = new Token();
        token.setId(id);
        token.setToken(jwt);
        token.setCreatedAt(Instant.now());
        token.setExpiresAt(jwtProvider.getExpiry(jwt));
        token.setActive(true);
        token.setUser(user);
        return tokenRepository.save(token);
    }

    public Token findByToken(String token) {
        return tokenRepository.findByToken(token).orElseThrow(() -> new NotFoundException("Token not found: " + token));
    }

    @Transactional
    public void deactivateToken(Token token) {
        token.setActive(false);
        tokenRepository.save(token);
    }

    @Transactional
    public Token rotateToken(Token token) {
        // mark old token inactive
        token.setActive(false);
        tokenRepository.save(token);
        // generate new token for same user
        return createTokenForUser(token.getUser());
    }

    public List<Token> findActiveOlderThan(Instant cutoff) {
        return tokenRepository.findByActiveTrueAndCreatedAtBefore(cutoff);
    }
}
