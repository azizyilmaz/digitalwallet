package com.aziz.digitalwallet.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class User {

    private UUID id;
    private String username;
    private String passwordHash;
    private Set<Role> roles;
}
