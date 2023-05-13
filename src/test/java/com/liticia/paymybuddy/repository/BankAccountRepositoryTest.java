package com.liticia.paymybuddy.repository;

import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Repository.BankAccountRepository;
import com.liticia.paymybuddy.security.AuthUser;
import com.liticia.paymybuddy.security.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/paymybuddytest?createDatabaseIfExist=true&useSSL=false&serverTimezone=UTC\n"
})
public class BankAccountRepositoryTest {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Test
    void getBankAccounts() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();

        assertEquals("IU12BONE", bankAccounts.get(1).getAccountNumber());
        assertEquals(5, bankAccounts.size());
    }

    @Test
    void testShouldSaveBankAccount() {
        BankAccount bankAccount = BankAccount.builder().accountNumber("IU13BONE").build();

        BankAccount saveBankAccount = bankAccountRepository.save(bankAccount);
        assertEquals("IU13BONE", saveBankAccount.getAccountNumber());
    }

    @Test
    void testShouldFindBankAccountWithAnAccountNumber() {
        Optional<BankAccount> accountNumber = bankAccountRepository.findByUserAndAccountNumber(User.builder().id(2).build(),"IU12BONE");
        assertTrue(accountNumber.isPresent());

        Optional<BankAccount> accountNumb = bankAccountRepository.findByUserAndAccountNumber(User.builder().id(2).build(), "IU1222BONE");
        assertTrue(accountNumb.isEmpty());
    }

    @Test
    void testShouldReturnBankAccountsOrderByCreatedAtDesc() {
        Pageable pageable = PageRequest.of(1, 2);
        Page<BankAccount> accountByCreatedAtDesc = bankAccountRepository.findAllByUserOrderByCreatedAtDesc(User.builder().id(2).build(), pageable);

        assertEquals(5, accountByCreatedAtDesc.getTotalElements());
        assertEquals(3, accountByCreatedAtDesc.getTotalPages());
    }

    @Test
    void testShouldReturnActiveBankAccounts() {
        List<BankAccount> bankAccounts = bankAccountRepository.findByUserAndActive(User.builder().id(2).build(), true);

        assertEquals(3, bankAccounts.size());
        assertEquals("IU13BONE", bankAccounts.get(0).getAccountNumber());
    }

}
