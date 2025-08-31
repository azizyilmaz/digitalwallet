package com.aziz.digitalwallet.web.controller;

import com.aziz.digitalwallet.domain.model.Customer;
import com.aziz.digitalwallet.service.application.CustomerService;
import com.aziz.digitalwallet.web.dto.CreateCustomerRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

    @Test
    void createCustomer_shouldReturnCustomer() throws Exception {
        Customer c = new Customer();
        c.setId(UUID.randomUUID());
        c.setName("Aziz");
        c.setSurname("Yılmaz");
        c.setTckn("12345678901");

        when(customerService.createCustomer(any(Customer.class))).thenReturn(c);

        CreateCustomerRequest req = new CreateCustomerRequest();
        req.setName("Aziz");
        req.setSurname("Yılmaz");
        req.setTckn("12345678901");

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Aziz"));
    }

    @Test
    void createCustomer_shouldFailValidationIfMissingFields() throws Exception {
        CreateCustomerRequest req = new CreateCustomerRequest(); // boş request

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }
}