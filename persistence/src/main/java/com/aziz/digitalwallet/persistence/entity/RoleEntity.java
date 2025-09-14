package com.aziz.digitalwallet.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class RoleEntity {

    @Id
    private UUID id;
    @Column(nullable = false, unique = true)
    private String name; // ROLE_CUSTOMER or ROLE_EMPLOYEE
}
