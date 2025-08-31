package com.aziz.digitalwallet.service.application;

import com.aziz.digitalwallet.common.exception.NotFoundException;
import com.aziz.digitalwallet.domain.enums.Currency;
import com.aziz.digitalwallet.domain.model.Customer;
import com.aziz.digitalwallet.domain.model.Wallet;
import com.aziz.digitalwallet.service.port.CustomerRepository;
import com.aziz.digitalwallet.service.port.WalletRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class WalletServiceTest {

    private final WalletRepository walletRepository = mock(WalletRepository.class);
    private final CustomerRepository customerRepository = mock(CustomerRepository.class);
    private final WalletService walletService = new WalletService(walletRepository, customerRepository);

    @Test
    void createWallet_shouldCreateWalletForExistingCustomer() {
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("Aziz");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Wallet wallet = new Wallet();
        wallet.setId(UUID.randomUUID());
        wallet.setCustomerId(customerId);
        wallet.setWalletName("MyWallet");
        wallet.setCurrency(Currency.TRY);
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setUsableBalance(BigDecimal.ZERO);

        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        Wallet result = walletService.createWallet(customerId, "MyWallet", Currency.TRY);

        assertThat(result.getWalletName()).isEqualTo("MyWallet");
        assertThat(result.getCurrency()).isEqualTo(Currency.TRY);
        assertThat(result.getCustomerId()).isEqualTo(customerId);

        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void createWallet_shouldThrowNotFoundWhenCustomerDoesNotExist() {
        UUID customerId = UUID.randomUUID();
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> walletService.createWallet(customerId, "MyWallet", Currency.USD))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Customer not found");
    }
}
