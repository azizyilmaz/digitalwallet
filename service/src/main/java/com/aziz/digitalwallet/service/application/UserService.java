package com.aziz.digitalwallet.service.application;

import com.aziz.digitalwallet.common.exception.NotFoundException;
import com.aziz.digitalwallet.domain.model.User;
import com.aziz.digitalwallet.service.port.UserRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findByUsername(@NotBlank String username) {
        return userRepository.findByUsername(username).
                orElseThrow(() -> new NotFoundException("User not found: " + username));
    }
}
