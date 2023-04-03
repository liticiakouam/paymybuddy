package com.liticia.paymybuddy.Service.impl;

import com.liticia.paymybuddy.Entity.Operation;
import com.liticia.paymybuddy.Entity.OperationType;
import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Repository.BankAccountRepository;
import com.liticia.paymybuddy.Repository.OperationRepository;
import com.liticia.paymybuddy.Repository.UserRepository;
import com.liticia.paymybuddy.Service.OperationService;
import com.liticia.paymybuddy.dto.OperationCreate;
import com.liticia.paymybuddy.exception.InsufficientBalanceException;
import com.liticia.paymybuddy.exception.UserNotExistException;
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

    @Override
    @Transactional
    public void saveCreditedAccount(OperationCreate operationCreate) {
        Optional<User> optionalUser = userRepository.findById(SecurityUtils.getCurrentUserId());
        if(optionalUser.isEmpty()) {
            throw new UserNotExistException();
        }
        User user = optionalUser.get();

        user.setBalance(user.getBalance() + operationCreate.getAmount());
        userRepository.save(user);

        Operation creditedOperation = Operation.builder().build();
        if (operationCreate.getOperationType() == OperationType.CREDIT) {
            creditedOperation.setOperationType(operationCreate.getOperationType());
            creditedOperation.setAmount(operationCreate.getAmount());
            creditedOperation.setUser(user);
            creditedOperation.setBankAccount(bankAccountRepository.findByAccountNumber(operationCreate.getAccountNumber()).get());
            creditedOperation.setOperationDate(new Date());
        }
        operationRepository.save(creditedOperation);


    }

    @Override
    @Transactional
    public void saveDebitedAccount(OperationCreate operationCreate) {
        Optional<User> optionalUser = userRepository.findById(SecurityUtils.getCurrentUserId());
        if(optionalUser.isEmpty()) {
            throw new UserNotExistException();
        }
        User user = optionalUser.get();

        if (user.getBalance() < operationCreate.getAmount()) {
            throw new InsufficientBalanceException();
        }
        user.setBalance(user.getBalance() - operationCreate.getAmount());
        userRepository.save(user);

        Operation debitedOperation = Operation.builder().build();
        if (operationCreate.getOperationType() == OperationType.DEBIT) {
            debitedOperation.setOperationType(operationCreate.getOperationType());
            debitedOperation.setAmount(operationCreate.getAmount());
            debitedOperation.setUser(user);
            debitedOperation.setBankAccount(bankAccountRepository.findByAccountNumber(operationCreate.getAccountNumber()).get());
            debitedOperation.setOperationDate(new Date());
        }
        operationRepository.save(debitedOperation);


    }

    @Override
    public Page<Operation> findPaginated(Pageable pageable) {
        return operationRepository.findAllByOrderByOperationDateDesc(pageable);
    }
}
