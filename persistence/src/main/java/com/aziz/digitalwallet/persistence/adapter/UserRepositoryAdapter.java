package com.aziz.digitalwallet.persistence.adapter;

import com.aziz.digitalwallet.domain.model.Role;
import com.aziz.digitalwallet.domain.model.User;
import com.aziz.digitalwallet.persistence.entity.RoleEntity;
import com.aziz.digitalwallet.persistence.repository.SpringDataUserRepository;
import com.aziz.digitalwallet.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final SpringDataUserRepository userRepository;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username).map(e -> {
            User user = new User();
            user.setUsername(e.getUsername());
            user.setId(e.getId());
            user.setPasswordHash(e.getPasswordHash());
            user.setRoles(e.getRoles().stream().map(this::toDomain).collect(Collectors.toSet()));
            return user;
        });
    }

    private Role toDomain(RoleEntity roleEntity) {
        Role role = new Role();
        role.setId(roleEntity.getId());
        role.setName(roleEntity.getName());
        return role;
    }
}
