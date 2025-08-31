package com.aziz.digitalwallet.service.application;

import com.aziz.digitalwallet.common.exception.NotFoundException;
import com.aziz.digitalwallet.domain.enums.Currency;
import com.aziz.digitalwallet.domain.model.Customer;
import com.aziz.digitalwallet.domain.model.Wallet;
import com.aziz.digitalwallet.service.port.CustomerRepository;
import com.aziz.digitalwallet.service.port.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public Wallet createWallet(UUID customerId, String walletName, Currency currency) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new NotFoundException("Customer not found: " + customerId));

        Wallet wallet = new Wallet();
        wallet.setId(UUID.randomUUID());
        wallet.setCustomerId(customer.getId());
        wallet.setWalletName(walletName);
        wallet.setCurrency(currency);
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setUsableBalance(BigDecimal.ZERO);
        wallet.setActiveForShopping(true);
        wallet.setActiveForWithdraw(true);

        return walletRepository.save(wallet);
    }

    public List<Wallet> getWalletsByCustomer(UUID customerId) {
        return walletRepository.findByCustomerId(customerId);
    }

    public Wallet getWallet(UUID id) {
        return walletRepository.findById(id).orElseThrow(() -> new NotFoundException("Wallet not found: " + id));
    }

}
