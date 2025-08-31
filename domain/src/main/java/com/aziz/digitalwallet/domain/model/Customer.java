package com.aziz.digitalwallet.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Customer {

    private UUID id;
    private String name;
    private String surname;
    private String tckn;

}
