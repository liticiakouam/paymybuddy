package com.liticia.paymybuddy.Service.impl;

import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Entity.Operation;
import com.liticia.paymybuddy.Entity.OperationType;
import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Repository.BankAccountRepository;
import com.liticia.paymybuddy.Repository.OperationRepository;
import com.liticia.paymybuddy.Repository.UserRepository;
import com.liticia.paymybuddy.Service.OperationService;
import com.liticia.paymybuddy.exception.BankAccountNotFoundException;
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
public class OperationServiceImpl implements OperationService {
    private final OperationRepository operationRepository;
    private final BankAccountRepository bankAccountRepository;

    private final UserRepository userRepository;

    public OperationServiceImpl(OperationRepository operationRepository, BankAccountRepository bankAccountRepository, UserRepository userRepository) {
        this.operationRepository = operationRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Operation> getAll() {
        return operationRepository.findAll();
    }

    @Transactional
    @Override
    public void creditAccount(float amount, String accountNumber) {
        Optional<User> optionalUser = userRepository.findById(SecurityUtils.getCurrentUserId());
        if(optionalUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = optionalUser.get();

        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);

        Optional<BankAccount> bankAccount = bankAccountRepository.findByAccountNumber(accountNumber);
        if (bankAccount.isEmpty()) {
            throw new BankAccountNotFoundException();
        }

        Operation creditedOperation = Operation.builder().build();
        creditedOperation.setAmount(amount);
        creditedOperation.setOperationType(OperationType.CREDIT);
        creditedOperation.setUser(user);
        creditedOperation.setBankAccount(bankAccount.get());
        creditedOperation.setOperationDate(new Date());

        operationRepository.save(creditedOperation);
    }

    @Transactional
    @Override
    public void debitAccount(float amount, String accountNumber) {
        Optional<User> optionalUser = userRepository.findById(SecurityUtils.getCurrentUserId());
        if(optionalUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = optionalUser.get();

        if (user.getBalance() < amount) {
            throw new InsufficientBalanceException();
        }
        user.setBalance(user.getBalance() - amount);
        userRepository.save(user);

        Optional<BankAccount> bankAccount = bankAccountRepository.findByAccountNumber(accountNumber);
        if (bankAccount.isEmpty()) {
            throw new BankAccountNotFoundException();
        }

        Operation debitedOperation = Operation.builder().build();
        debitedOperation.setOperationType(OperationType.DEBIT);
        debitedOperation.setAmount(amount);
        debitedOperation.setUser(user);
        debitedOperation.setBankAccount(bankAccount.get());
        debitedOperation.setOperationDate(new Date());
        operationRepository.save(debitedOperation);
    }

    @Override
    public Page<Operation> findPaginated(Pageable pageable) {
        return operationRepository.findAllByOrderByOperationDateDesc(pageable);
    }
}
