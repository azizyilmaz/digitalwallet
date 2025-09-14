package com.aziz.digitalwallet.web.config;

import com.aziz.digitalwallet.domain.model.Token;
import com.aziz.digitalwallet.service.application.TokenService;
import com.aziz.digitalwallet.service.component.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws jakarta.servlet.ServletException, IOException {
        String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }
        String token = header.substring(7);
        if (!jwtProvider.validateToken(token)) {
            chain.doFilter(req, res); // invalid token => unauthenticated
            return;
        }
        // check token exists & active in DB
        Token t = tokenService.findByToken(token);
        if (t == null || !t.isActive() || t.getExpiresAt().isBefore(java.time.Instant.now())) {
            chain.doFilter(req, res);
            return;
        }

        Claims claims = jwtProvider.parseClaims(token).getBody();
        String username = claims.getSubject(); // userId
        List<String> roles = claims.get("roles", List.class);

        var authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        var auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
        // set principal username to subject (userId string)
        auth.setDetails(username);

        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(req, res);
    }
}
