package com.aziz.digitalwallet.service.application;

import com.aziz.digitalwallet.common.exception.NotFoundException;
import com.aziz.digitalwallet.domain.model.Customer;
import com.aziz.digitalwallet.service.port.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE') or principal == #customerId.toString()")
    public Customer getCustomer(UUID customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> new NotFoundException("Customer not found: " + customerId));
    }

}
