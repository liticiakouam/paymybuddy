package com.liticia.paymybuddy.service;

import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Repository.BankAccountRepository;
import com.liticia.paymybuddy.Repository.UserRepository;
import com.liticia.paymybuddy.Service.BankAccountService;
import com.liticia.paymybuddy.Service.impl.BankAccountServiceImpl;
import com.liticia.paymybuddy.dto.BankAccountCreate;
import com.liticia.paymybuddy.exception.BankAccountAlreadyExist;
import com.liticia.paymybuddy.exception.BankAccountNotExistException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BankAccountServiceImplTest {

    private final BankAccountRepository bankAccountRepository = Mockito.mock(BankAccountRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final BankAccountService bankAccountService = new BankAccountServiceImpl(bankAccountRepository, userRepository);

    @Test
    void testShouldReturnBankAccounts() {
        List<BankAccount> list = Arrays.asList(
                BankAccount.builder().active(true).build(),
                BankAccount.builder().description("momo").build()
        ); 
        when(bankAccountRepository.findAll()).thenReturn(list);

        List<BankAccount> bankAccounts = bankAccountService.getAll();
        assertEquals(2, bankAccounts.size());
        verify(bankAccountRepository, times(1)).findAll();
    }

    @Test
    void testShouldThrowExceptionWhenBankAccountAlreadyExist() {
        BankAccount bankAccount = BankAccount.builder().accountNumber("IU13BONE").build();
        when(bankAccountRepository.findByAccountNumber("IU13BONE")).thenReturn(Optional.of(bankAccount));

        assertThrows(BankAccountAlreadyExist.class, ()->bankAccountService.save(BankAccountCreate.builder().accountNumber("IU13BONE").build()));

        verify(bankAccountRepository, times(1)).findByAccountNumber("IU13BONE");
    }

    @Test
    void testShouldSaveBankAccount() {
        BankAccountCreate bankAccountCreate = BankAccountCreate.builder().accountNumber("IU22BONE").build();

        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber(bankAccountCreate.getAccountNumber());
        when(bankAccountRepository.save(bankAccount)).thenReturn(bankAccount);

        bankAccountService.save(bankAccountCreate);
    }

    @Test
    void testShouldThrowExceptionWhenBankAccountNotExist() {
        when(bankAccountRepository.findByAccountNumber("IU13BONE")).thenReturn(Optional.empty());
        assertThrows(BankAccountNotExistException.class, ()->bankAccountService.switchAccountStatus("IU13BONE"));

        verify(bankAccountRepository, times(1)).findByAccountNumber("IU13BONE");
    }

    @Test
    void testShouldChangeExistingStatusOfTheBankAccount() {
        BankAccount bankAccount = BankAccount.builder().accountNumber("IU13BONE").active(true).build();
        bankAccount.setActive(!bankAccount.isActive());

        when(bankAccountRepository.findByAccountNumber("IU13BONE")).thenReturn(Optional.of(bankAccount));
        when(bankAccountRepository.save(bankAccount)).thenReturn(bankAccount);

        bankAccountService.switchAccountStatus("IU13BONE");
        verify(bankAccountRepository, times(1)).findByAccountNumber("IU13BONE");
        verify(bankAccountRepository, times(1)).save(bankAccount);
    }

    @Test
    void testShouldFindActiveBankAccount() {
        List<BankAccount> list = Arrays.asList(
                BankAccount.builder().active(true).build(),
                BankAccount.builder().active(true).user(User.builder().id(2).build()).build()
        );
        when(bankAccountRepository.findByActive(anyBoolean())).thenReturn(list);

        List<BankAccount> activeBankAccounts = bankAccountService.findActiveAccountNumber(true);

        assertEquals(2, activeBankAccounts.size());
        verify(bankAccountRepository, times(1)).findByActive(true);
    }
}
