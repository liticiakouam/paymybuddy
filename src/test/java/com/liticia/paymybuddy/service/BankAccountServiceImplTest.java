package com.liticia.paymybuddy.service;

import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Repository.BankAccountRepository;
import com.liticia.paymybuddy.Repository.UserRepository;
import com.liticia.paymybuddy.Service.BankAccountService;
import com.liticia.paymybuddy.Service.impl.BankAccountServiceImpl;
import com.liticia.paymybuddy.dto.BankAccountCreate;
import com.liticia.paymybuddy.exception.BankAccountAlreadyExist;
import com.liticia.paymybuddy.security.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BankAccountServiceImplTest {

    private final BankAccountRepository bankAccountRepository = Mockito.mock(BankAccountRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final Authentication authentication = Mockito.mock(Authentication.class);
    private final BankAccountService bankAccountService = new BankAccountServiceImpl(bankAccountRepository, userRepository);
    MockedStatic<SecurityUtils> securityUtils = Mockito.mockStatic(SecurityUtils.class);

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
        User user = User.builder().firstname("liticia").lastname("anz").balance(1000).build();
        BankAccount bankAccount = BankAccount.builder().accountNumber("IU13BONE").build();

        securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
        when(bankAccountRepository.findByUserAndAccountNumber(user,"IU13BONE")).thenReturn(Optional.of(bankAccount));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(BankAccountAlreadyExist.class, ()->bankAccountService.save(BankAccountCreate.builder().accountNumber("IU13BONE").build()));

        verify(bankAccountRepository, times(1)).findByUserAndAccountNumber(user, "IU13BONE");
    }

    @Test
    void testShouldSaveBankAccount() {
        BankAccountCreate bankAccountCreate = BankAccountCreate.builder().accountNumber("IU22BONE").build();
        User user = User.builder().id(2L).balance(2000).build();

        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber(bankAccountCreate.getAccountNumber());
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        bankAccountService.save(bankAccountCreate);
    }

    @Test
    void testShouldThrowExceptionWhenBankAccountNotExist() {
        BankAccount bankAccount = BankAccount.builder().accountNumber("IU13BONE").active(true).build();
        bankAccount.setActive(!bankAccount.isActive());
        User user = User.builder().firstname("liticia").lastname("anz").balance(1000).build();

        securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
        when(bankAccountRepository.findByUserAndAccountNumber(user,"IU19BONE")).thenReturn(Optional.of(bankAccount));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        bankAccountService.switchAccountStatus("IU13BONE");
        verify(bankAccountRepository, times(1)).findByUserAndAccountNumber(User.builder().build(),"IU13BONE");
        verify(bankAccountRepository, times(1)).save(bankAccount);
    }

    @Test
    void testShouldChangeExistingStatusOfTheBankAccount() {
        BankAccount bankAccount = BankAccount.builder().accountNumber("IU13BONE").active(true).build();
        bankAccount.setActive(!bankAccount.isActive());
        User user = User.builder().firstname("liticia").lastname("anz").balance(1000).build();

        securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
        when(bankAccountRepository.findByUserAndAccountNumber(user,"IU13BONE")).thenReturn(Optional.of(bankAccount));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        bankAccountService.switchAccountStatus("IU13BONE");
        verify(bankAccountRepository, times(1)).save(bankAccount);
    }

    @Test
    void testShouldFindActiveBankAccount() {
        List<BankAccount> list = Arrays.asList(
                BankAccount.builder().active(true).build(),
                BankAccount.builder().active(true).user(User.builder().id(2).build()).build()
        );
        User user = User.builder().firstname("liticia").lastname("anz").balance(1000).build();

        securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bankAccountRepository.findByUserAndActive(user, true)).thenReturn(list);

        List<BankAccount> activeBankAccounts = bankAccountService.findActiveBankAccount();

        assertEquals(2, activeBankAccounts.size());
    }
}
