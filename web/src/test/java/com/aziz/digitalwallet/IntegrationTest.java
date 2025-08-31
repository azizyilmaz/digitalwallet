package com.aziz.digitalwallet;

import com.aziz.digitalwallet.domain.enums.Currency;
import com.aziz.digitalwallet.domain.enums.TransactionStatus;
import com.aziz.digitalwallet.domain.model.Customer;
import com.aziz.digitalwallet.domain.model.Transaction;
import com.aziz.digitalwallet.domain.model.Wallet;
import com.aziz.digitalwallet.service.application.CustomerService;
import com.aziz.digitalwallet.service.application.TransactionService;
import com.aziz.digitalwallet.service.application.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = DigitalWalletApplication.class)
@Transactional
class IntegrationTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private TransactionService transactionService;

    @Test
    void customer_wallet_transaction_flow_shouldWorkEndToEnd() {
        // Customer oluştur
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setName("Aziz");
        customer.setSurname("Yılmaz");
        customer.setTckn("12345678901");

        Customer savedCustomer = customerService.createCustomer(customer);
        assertThat(savedCustomer.getId()).isNotNull();

        // Wallet oluştur
        Wallet wallet = walletService.createWallet(savedCustomer.getId(), "MyWallet", Currency.TRY);
        assertThat(wallet.getCustomerId()).isEqualTo(savedCustomer.getId());
        assertThat(wallet.getBalance()).isEqualByComparingTo("0");

        // Deposit yap
        Transaction depositTx = transactionService.deposit(wallet.getId(), BigDecimal.valueOf(500), "TR123", "IBAN");
        assertThat(depositTx.getStatus()).isEqualTo(TransactionStatus.APPROVED);

        // Withdraw yap
        Transaction withdrawTx = transactionService.withdraw(wallet.getId(), BigDecimal.valueOf(200), "TR456", "IBAN");
        assertThat(withdrawTx.getStatus()).isEqualTo(TransactionStatus.APPROVED);

        // Wallet güncel bakiyeyi kontrol et
        Wallet updatedWallet = walletService.getWallet(wallet.getId());
        assertThat(updatedWallet.getBalance()).isEqualByComparingTo("300"); // 500 - 200
        assertThat(updatedWallet.getUsableBalance()).isEqualByComparingTo("300");
    }
}
