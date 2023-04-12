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
    public Page<Transaction> findPaginated(Pageable pageable) {
        return transactionRepository.findAllByOrderByTransactionDateDesc(pageable);
    }

    @Transactional
    @Override
    public void save(TransactionCreate transactionCreate) {
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
        transaction.setContact(optionalContact.get());
        transaction.setDebitedAmount(debitedAmount);
        transaction.setAmount(transactionCreate.getAmount());
        transaction.setTransactionDate(new Date());

        transactionRepository.save(transaction);
    }
}
