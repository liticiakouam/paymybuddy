package com.liticia.paymybuddy.Service.impl;

import com.liticia.paymybuddy.Entity.Contact;
import com.liticia.paymybuddy.Entity.Transaction;
import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Repository.ContactRepository;
import com.liticia.paymybuddy.Repository.TransactionRepository;
import com.liticia.paymybuddy.Repository.UserRepository;
import com.liticia.paymybuddy.Service.TransactionService;
import com.liticia.paymybuddy.dto.TransactionCreate;
import com.liticia.paymybuddy.exception.ContactNotFoundException;
import com.liticia.paymybuddy.exception.InsufficientBalanceException;
import com.liticia.paymybuddy.exception.UserNotFoundException;
import com.liticia.paymybuddy.security.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, ContactRepository contactRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    @Override
    public Page<Transaction> findAll(Pageable pageable) {
        Optional<User> currentUser = userRepository.findById(SecurityUtils.getCurrentUserId());

        return transactionRepository.findAllByPrincipalUserOrderByTransactionDateDesc(currentUser.get(), pageable);

    }

    @Transactional
    @Override
    public void save(TransactionCreate transactionCreate) {
        Optional<User> optionalUser = userRepository.findById(SecurityUtils.getCurrentUserId());
        if(optionalUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        User currentUser = optionalUser.get();

        Optional<Contact> optionalContact = contactRepository.findById(transactionCreate.getContactId());
        if (optionalContact.isEmpty()) {
            throw new ContactNotFoundException();
        }

        User userFriend = optionalContact.get().getUserFriend();

        User user = optionalContact.get().getUser();
        if (user.getBalance() < transactionCreate.getAmount()) {
            throw new InsufficientBalanceException();
        }
        float debitedAmount = (float) (transactionCreate.getAmount() * 0.005);
        user.setBalance(user.getBalance() - transactionCreate.getAmount() - debitedAmount);
        userRepository.save(user);

        userFriend.setBalance(userFriend.getBalance() + transactionCreate.getAmount());
        userRepository.save(userFriend);

        Transaction transaction = Transaction.builder().build();
        transaction.setSubject(transactionCreate.getSubject());
        transaction.setFriendUser(userFriend);
        transaction.setDebitedAmount(debitedAmount);
        transaction.setAmount(transactionCreate.getAmount());
        transaction.setTransactionDate(new Date());
        transaction.setPrincipalUser(currentUser);

        transactionRepository.save(transaction);

        Transaction transaction1 = Transaction.builder().build();
        transaction1.setSubject(transactionCreate.getSubject());
        transaction1.setFriendUser(currentUser);
        transaction1.setDebitedAmount(debitedAmount);
        transaction1.setAmount(transactionCreate.getAmount());
        transaction1.setTransactionDate(new Date());
        transaction1.setPrincipalUser(userFriend);

        transactionRepository.save(transaction1);
    }
}
