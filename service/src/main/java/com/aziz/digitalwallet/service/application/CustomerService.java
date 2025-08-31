package com.aziz.digitalwallet.service.application;

import com.aziz.digitalwallet.common.exception.NotFoundException;
import com.aziz.digitalwallet.domain.model.Customer;
import com.aziz.digitalwallet.service.port.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer getCustomer(UUID id) {
        return customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer not found: " + id));
    }

}
