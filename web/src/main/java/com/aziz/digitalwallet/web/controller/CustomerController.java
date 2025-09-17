package com.aziz.digitalwallet.web.controller;

import com.aziz.digitalwallet.domain.model.Customer;
import com.aziz.digitalwallet.service.application.CustomerService;
import com.aziz.digitalwallet.web.dto.CreateCustomerRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setName(request.getName());
        customer.setSurname(request.getSurname());
        customer.setTckn(request.getTckn());
        return ResponseEntity.ok(customerService.createCustomer(customer));
    }

    @GetMapping("/id/{customerId}")
    public ResponseEntity<Customer> getCustomer(@PathVariable UUID customerId) {
        return ResponseEntity.ok(customerService.getCustomer(customerId));
    }

}