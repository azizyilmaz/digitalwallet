package com.aziz.digitalwallet.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Role {

    private UUID id;
    private String name; // ROLE_CUSTOMER or ROLE_EMPLOYEE

}
