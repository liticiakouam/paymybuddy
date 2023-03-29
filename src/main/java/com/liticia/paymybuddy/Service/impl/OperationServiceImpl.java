package com.liticia.paymybuddy.Service.impl;

import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Entity.Operation;
import com.liticia.paymybuddy.Entity.OperationType;
import com.liticia.paymybuddy.Repository.BankAccountRepository;
import com.liticia.paymybuddy.Repository.OperationRepository;
import com.liticia.paymybuddy.Service.OperationService;
import com.liticia.paymybuddy.dto.OperationCreate;
import com.liticia.paymybuddy.exception.BankAccountNotExist;
import com.liticia.paymybuddy.security.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OperationServiceImpl implements OperationService {
    private final OperationRepository operationRepository;
    private final BankAccountRepository bankAccountRepository;

    public OperationServiceImpl(OperationRepository operationRepository, BankAccountRepository bankAccountRepository) {
        this.operationRepository = operationRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public List<Operation> getAll() {
        return operationRepository.findAll();
    }

    @Override
    public void save(OperationCreate operationCreate) {
        Optional<BankAccount> bankAccount = bankAccountRepository.findByAccountNumber(operationCreate.getAccountNumber());
        if (bankAccount.isEmpty()) {
            throw new BankAccountNotExist();
        }
            Operation operation = Operation.builder().build();

            if (operationCreate.getOperationType() == OperationType.CREDIT) {
                operation.setOperationType(operationCreate.getOperationType());
                operation.setAmount(operationCreate.getAmount() + operationCreate.getAmount());
                operation.setId(SecurityUtils.getCurrentUserId());
                operation.setBankAccount(bankAccount.get());
                operation.setOperationDate(new Date());
            }
                operation.setOperationType(operationCreate.getOperationType());
                operation.setAmount(operation.getAmount() - operationCreate.getAmount());
                operation.setId(SecurityUtils.getCurrentUserId());
                operation.setBankAccount(bankAccount.get());
                operation.setOperationDate(new Date());


        operationRepository.save(operation);
    }

    @Override
    public Page<Operation> findPaginated(Pageable pageable) {
        return operationRepository.findAllByOrderByOperationDateDesc(pageable);
    }
}
