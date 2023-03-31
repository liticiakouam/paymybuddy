package com.liticia.paymybuddy.Service.impl;

import com.liticia.paymybuddy.Entity.Operation;
import com.liticia.paymybuddy.Entity.OperationType;
import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Repository.BankAccountRepository;
import com.liticia.paymybuddy.Repository.OperationRepository;
import com.liticia.paymybuddy.Repository.UserRepository;
import com.liticia.paymybuddy.Service.OperationService;
import com.liticia.paymybuddy.dto.OperationCreate;
import com.liticia.paymybuddy.exception.OperationFailed;
import com.liticia.paymybuddy.exception.UserNotExist;
import com.liticia.paymybuddy.security.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = OperationFailed.class)
    public void saveCreditedAccount(OperationCreate operationCreate) throws Exception {
        Optional<User> optionalUser = userRepository.findById(SecurityUtils.getCurrentUserId());
        if(optionalUser.isEmpty()) {
            throw new UserNotExist();
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
        } else {
            throw new OperationFailed();
        }

        operationRepository.save(creditedOperation);
    }

    @Override
    public Page<Operation> findPaginated(Pageable pageable) {
        return operationRepository.findAllByOrderByOperationDateDesc(pageable);
    }
}
