package com.liticia.paymybuddy.service;

import com.liticia.paymybuddy.Entity.*;
import com.liticia.paymybuddy.Repository.*;
import com.liticia.paymybuddy.Service.OperationService;
import com.liticia.paymybuddy.Service.TransactionService;
import com.liticia.paymybuddy.Service.impl.OperationServiceImpl;
import com.liticia.paymybuddy.Service.impl.TransactionServiceImpl;
import com.liticia.paymybuddy.dto.OperationCreate;
import com.liticia.paymybuddy.dto.TransactionCreate;
import com.liticia.paymybuddy.exception.ContactNotFoundException;
import com.liticia.paymybuddy.exception.InsufficientBalanceException;
import com.liticia.paymybuddy.exception.UserNotFoundException;
import com.liticia.paymybuddy.security.SecurityUtils;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;

import java.security.Security;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TransactionServiceImplTest {

    private final ContactRepository contactRepository = Mockito.mock(ContactRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);
    MockedStatic<SecurityUtils> securityUtils = Mockito.mockStatic(SecurityUtils.class);

    private final TransactionService transactionService = new TransactionServiceImpl(transactionRepository, contactRepository, userRepository);

    @Test
    void testShouldReturnTransactions() {
        List<Transaction> list = Arrays.asList(
                Transaction.builder().amount(2000).id(2).build(),
                Transaction.builder().amount(5000).id(1).build()
        );
        when(transactionRepository.findAll()).thenReturn(list);

        List<Transaction> transactions = transactionService.getAll();
        assertEquals(2, transactions.size());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void testShouldSaveTransaction()  {
        User user = User.builder().firstname("liticia").lastname("anz").balance(1000).build();
        User userFriend = User.builder().firstname("lit").lastname("anz").balance(2000).build();

        TransactionCreate transactionCreate = TransactionCreate.builder().contactId(1).amount(500).build();
        Contact contact = Contact.builder().id(1).user(user).userFriend(userFriend).build();

        securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(userFriend));
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));

        transactionService.save(transactionCreate);
    }

    @Test
    void testShouldThrowInsufficientBalanceException()  {
        User user = User.builder().id(1L).firstname("liticia").lastname("anz").balance(1000).build();
        User userFriend = User.builder().id(2L).firstname("lit").lastname("anz").balance(2000).build();

        TransactionCreate transactionCreate = TransactionCreate.builder().contactId(1).subject("depot").amount(5000).build();
        Contact contact = Contact.builder().id(1).user(user).userFriend(userFriend).build();

        securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(userFriend));
        assertThrows(InsufficientBalanceException.class, ()->transactionService.save(transactionCreate));
    }

    @Test
    void testShouldThrowContactNotFoundException()  {
        Contact contact = Contact.builder().id(2).build();
        User user = User.builder().id(1L).firstname("liticia").lastname("anz").balance(1000).build();
        User userFriend = User.builder().id(1L).firstname("lit").lastname("anz").balance(2000).build();

        TransactionCreate transactionCreate = TransactionCreate.builder().contactId(2).subject("depot").amount(5000).build();

        securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(userFriend));
        when(contactRepository.findById(5L)).thenReturn(Optional.of(contact));

        assertThrows(ContactNotFoundException.class, ()->transactionService.save(transactionCreate));
    }

}
