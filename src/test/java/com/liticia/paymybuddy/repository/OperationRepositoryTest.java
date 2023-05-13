package com.liticia.paymybuddy.repository;

import com.liticia.paymybuddy.Entity.Operation;
import com.liticia.paymybuddy.Entity.OperationType;
import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Repository.OperationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/paymybuddytest?createDatabaseIfExist=true&useSSL=false&serverTimezone=UTC\n"
})
public class OperationRepositoryTest {
    @Autowired
    private OperationRepository operationRepository;

    @Test
    void testShouldSaveBankAccount() {
        Operation operation = Operation.builder().amount(10000).operationType(OperationType.CREDIT).build();
        Operation operationSave = operationRepository.save(operation);

        assertEquals(OperationType.CREDIT, operationSave.getOperationType());
        assertEquals(10000.0, operationSave.getAmount());
    }

    @Test
    void testShouldReturnBankAccountsOrderByCreatedAtDesc() {
        Pageable pageable = PageRequest.of(1, 2);
        Page<Operation> accountByCreatedAtDesc = operationRepository.findAllByUserOrderByOperationDateDesc(User.builder().build(), pageable);

        assertEquals(2, accountByCreatedAtDesc.getTotalElements());
        assertEquals(1, accountByCreatedAtDesc.getTotalPages());
    }

}
