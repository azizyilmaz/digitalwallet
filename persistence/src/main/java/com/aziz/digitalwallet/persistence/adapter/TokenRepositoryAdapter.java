package com.aziz.digitalwallet.persistence.adapter;

import com.aziz.digitalwallet.domain.model.Role;
import com.aziz.digitalwallet.domain.model.Token;
import com.aziz.digitalwallet.domain.model.User;
import com.aziz.digitalwallet.persistence.entity.RoleEntity;
import com.aziz.digitalwallet.persistence.entity.TokenEntity;
import com.aziz.digitalwallet.persistence.entity.UserEntity;
import com.aziz.digitalwallet.persistence.repository.SpringDataTokenRepository;
import com.aziz.digitalwallet.service.port.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TokenRepositoryAdapter implements TokenRepository {

    private final SpringDataTokenRepository tokenRepository;

    @Override
    public Token save(Token token) {
        TokenEntity entity = toEntity(token);
        TokenEntity saved = tokenRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Token> findByToken(String token) {
        return tokenRepository.findByToken(token).map(e -> {
            Token t = new Token();
            t.setId(e.getId());
            t.setUser(toUserDomain(e.getUser()));
            t.setActive(e.isActive());
            t.setToken(e.getToken());
            t.setExpiresAt(e.getExpiresAt());
            t.setCreatedAt(e.getCreatedAt());
            return t;
        });
    }

    @Override
    public List<Token> findByActiveTrueAndCreatedAtBefore(Instant cutoff) {
        return tokenRepository.findByActiveTrueAndCreatedAtBefore(cutoff)
                .stream().map(this::toDomain)
                .collect(Collectors.toList());
    }

    private TokenEntity toEntity(Token t) {
        TokenEntity te = new TokenEntity();
        te.setId(t.getId());
        te.setToken(t.getToken());
        te.setActive(t.isActive());
        te.setCreatedAt(t.getCreatedAt());
        te.setExpiresAt(t.getExpiresAt());
        te.setUser(toUserEntity(t.getUser()));
        return te;
    }

    private UserEntity toUserEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setRoles(toRoleEntity(user.getRoles()));
        userEntity.setUsername(user.getUsername());
        userEntity.setPasswordHash(user.getPasswordHash());
        return userEntity;
    }

    private Set<RoleEntity> toRoleEntity(Set<Role> roles) {
        Set<RoleEntity> roleEntities = new HashSet<>();
        for (Role role : roles) {
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setId(role.getId());
            roleEntity.setName(role.getName());
            roleEntities.add(roleEntity);
        }
        return roleEntities;
    }

    private Token toDomain(TokenEntity te) {
        Token t = new Token();
        t.setId(te.getId());
        t.setToken(te.getToken());
        t.setExpiresAt(te.getExpiresAt());
        t.setCreatedAt(te.getCreatedAt());
        t.setActive(te.isActive());
        t.setUser(toUserDomain(te.getUser()));
        return t;
    }

    private User toUserDomain(UserEntity userEntity) {
        User user = new User();
        user.setUsername(userEntity.getUsername());
        user.setId(userEntity.getId());
        user.setPasswordHash(userEntity.getPasswordHash());
        user.setRoles(toRoleDomain(userEntity.getRoles()));
        return user;
    }

    private Set<Role> toRoleDomain(Set<RoleEntity> roleEntities) {
        Set<Role> roles = new HashSet<>();
        for (RoleEntity roleEntity : roleEntities) {
            Role role = new Role();
            role.setName(roleEntity.getName());
            role.setId(roleEntity.getId());
            roles.add(role);
        }
        return roles;
    }
}
