package com.aziz.digitalwallet.service.port;

import com.aziz.digitalwallet.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByUsername(String username);
}
