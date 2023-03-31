package com.liticia.paymybuddy.service;

import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Entity.Operation;
import com.liticia.paymybuddy.Entity.OperationType;
import com.liticia.paymybuddy.Repository.BankAccountRepository;
import com.liticia.paymybuddy.Repository.OperationRepository;
import com.liticia.paymybuddy.Repository.UserRepository;
import com.liticia.paymybuddy.Service.OperationService;
import com.liticia.paymybuddy.Service.impl.OperationServiceImpl;
import com.liticia.paymybuddy.dto.OperationCreate;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class OperationServiceImplTest {

    private final BankAccountRepository bankAccountRepository = Mockito.mock(BankAccountRepository.class);
    private final OperationRepository operationRepository = Mockito.mock(OperationRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final OperationService operationService = new OperationServiceImpl(operationRepository, bankAccountRepository, userRepository);

    @Test
    void testShouldReturnOperations() {
        List<Operation> list = Arrays.asList(
                Operation.builder().operationType(OperationType.CREDIT).build(),
                Operation.builder().amount(2000.0).build()
        ); 
        when(operationRepository.findAll()).thenReturn(list);

        List<Operation> operations = operationService.getAll();
        assertEquals(2, operations.size());
        verify(operationRepository, times(1)).findAll();
    }

    @Test
    void testShouldSaveOperation() {
        OperationCreate operationCreate = OperationCreate.builder().operationType(OperationType.DEBIT).accountNumber("IU13BONE").amount(200000.0).build();

        Operation operation = new Operation();
        operation.setAmount(operationCreate.getAmount());

        when(bankAccountRepository.findByAccountNumber("IU13BONE")).thenReturn(Optional.of(BankAccount.builder().build()));
        when(operationRepository.save(operation)).thenReturn(operation);

        operationService.saveCreditedAccount(operationCreate);
    }
}
