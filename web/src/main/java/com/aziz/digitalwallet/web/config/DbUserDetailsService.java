package com.aziz.digitalwallet.web.config;

import com.aziz.digitalwallet.common.exception.NotFoundException;
import com.aziz.digitalwallet.persistence.entity.UserEntity;
import com.aziz.digitalwallet.persistence.repository.SpringDataUserRepository;
import com.aziz.digitalwallet.service.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DbUserDetailsService implements UserDetailsService {

    private final SpringDataUserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity u = userRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found: " + username));
        var authorities = u.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());
        // We'll set username to the user's id (string) so SpEL can compare principal.username with customerId.toString()
        return new AppUserDetails(u.getId(), u.getId().toString(), u.getPasswordHash(), authorities);
    }
}