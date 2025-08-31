package com.aziz.digitalwallet.service.port;

import com.aziz.digitalwallet.domain.model.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {

    Customer save(Customer customer);

    Optional<Customer> findById(UUID id);

}
