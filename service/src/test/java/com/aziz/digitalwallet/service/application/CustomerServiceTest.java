package com.aziz.digitalwallet.service.application;

import com.aziz.digitalwallet.common.exception.NotFoundException;
import com.aziz.digitalwallet.domain.model.Customer;
import com.aziz.digitalwallet.service.port.CustomerRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    private final CustomerRepository customerRepository = mock(CustomerRepository.class);
    private final CustomerService customerService = new CustomerService(customerRepository);

    @Test
    void createCustomer_shouldSaveCustomer() {
        Customer c = new Customer();
        c.setId(UUID.randomUUID());
        c.setName("Aziz");
        c.setSurname("Yılmaz");
        c.setTckn("12345678901");

        when(customerRepository.save(c)).thenReturn(c);

        Customer result = customerService.createCustomer(c);

        assertThat(result.getName()).isEqualTo("Aziz");
        verify(customerRepository, times(1)).save(c);
    }

    @Test
    void getCustomer_shouldReturnCustomerIfExists() {
        UUID id = UUID.randomUUID();
        Customer c = new Customer();
        c.setId(id);
        c.setName("Aziz");

        when(customerRepository.findById(id)).thenReturn(Optional.of(c));

        Customer result = customerService.getCustomer(id);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("Aziz");
    }

    @Test
    void getCustomer_shouldThrowNotFoundIfMissing() {
        UUID id = UUID.randomUUID();

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.getCustomer(id)).isInstanceOf(NotFoundException.class).hasMessageContaining("Customer not found");
    }
}
