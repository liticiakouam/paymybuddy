package com.liticia.paymybuddy.repository;

import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Entity.Transaction;
import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Repository.TransactionRepository;
import com.liticia.paymybuddy.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/paymybuddytest?createDatabaseIfExist=true&useSSL=false&serverTimezone=UTC\n"
})
public class TransactionRepositoryTest {
    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void testShouldReturnBankAccountsOrderByCreatedAtDesc() {
        Pageable pageable = PageRequest.of(1, 2);
        Page<Transaction> transactions = transactionRepository.findAllByOrderByTransactionDateDesc(pageable);

        assertEquals(2, transactions.getTotalElements());
        assertEquals(1, transactions.getTotalPages());
    }

}
