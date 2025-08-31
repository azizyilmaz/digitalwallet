package com.aziz.digitalwallet.persistence.adapter;

import com.aziz.digitalwallet.domain.model.Customer;
import com.aziz.digitalwallet.persistence.entity.CustomerEntity;
import com.aziz.digitalwallet.persistence.repository.SpringDataCustomerRepository;
import com.aziz.digitalwallet.service.port.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepository {

    private final SpringDataCustomerRepository repo;

    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = new CustomerEntity();
        entity.setId(customer.getId());
        entity.setName(customer.getName());
        entity.setSurname(customer.getSurname());
        entity.setTckn(customer.getTckn());
        CustomerEntity saved = repo.save(entity);

        Customer result = new Customer();
        result.setId(saved.getId());
        result.setName(saved.getName());
        result.setSurname(saved.getSurname());
        result.setTckn(saved.getTckn());
        return result;
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return repo.findById(id).map(e -> {
            Customer c = new Customer();
            c.setId(e.getId());
            c.setName(e.getName());
            c.setSurname(e.getSurname());
            c.setTckn(e.getTckn());
            return c;
        });
    }
}