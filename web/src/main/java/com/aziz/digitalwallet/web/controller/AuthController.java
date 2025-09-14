package com.aziz.digitalwallet.web.controller;

import com.aziz.digitalwallet.domain.model.Token;
import com.aziz.digitalwallet.service.application.TokenService;
import com.aziz.digitalwallet.service.application.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest req) {
        var user = userService.findByUsername(req.username());
        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) return ResponseEntity.status(401).build();

        var token = tokenService.createTokenForUser(user);
        return ResponseEntity.ok(Map.of(
                "token", token.getToken(),
                "expiresAt", token.getExpiresAt().toString(),
                "userId", user.getId().toString()
        ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refresh(@RequestHeader("Authorization") String bearer) {
        if (bearer == null || !bearer.startsWith("Bearer ")) return ResponseEntity.badRequest().build();
        String token = bearer.substring(7);
        Token t = tokenService.findByToken(token);
        // ensure token active
        if (!t.isActive()) return ResponseEntity.status(401).build();
        var newToken = tokenService.rotateToken(t);
        return ResponseEntity.ok(Map.of("token", newToken.getToken(), "expiresAt", newToken.getExpiresAt().toString()));
    }

    record LoginRequest(@NotBlank String username, @NotBlank String password) {
    }
}