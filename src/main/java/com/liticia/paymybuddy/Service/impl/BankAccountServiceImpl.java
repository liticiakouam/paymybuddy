package com.liticia.paymybuddy.Service.impl;

import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Repository.BankAccountRepository;
import com.liticia.paymybuddy.Repository.UserRepository;
import com.liticia.paymybuddy.Service.BankAccountService;
import com.liticia.paymybuddy.dto.BankAccountCreate;
import com.liticia.paymybuddy.exception.BankAccountAlreadyExistException;
import com.liticia.paymybuddy.exception.BankAccountNotExistException;
import com.liticia.paymybuddy.exception.UserNotExistException;
import com.liticia.paymybuddy.security.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, UserRepository userRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<BankAccount> getAll() {
        return bankAccountRepository.findAll();
    }

    @Override
    public void save(BankAccountCreate bankAccountCreate) {
        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findByAccountNumber(bankAccountCreate.getAccountNumber());
        if (optionalBankAccount.isPresent()) {
            throw new BankAccountAlreadyExistException();
        }

        Optional<User> optionalUser = userRepository.findById(SecurityUtils.getCurrentUserId());
        if (optionalUser.isEmpty()) {
            throw new UserNotExistException();
        }

        BankAccount bankAccount = new BankAccount();
        bankAccount.setDescription(bankAccountCreate.getDescription());
        bankAccount.setAccountNumber(bankAccountCreate.getAccountNumber());
        bankAccount.setCreatedAt(new Date());
        bankAccount.setUser(optionalUser.get());

        bankAccountRepository.save(bankAccount);
    }

    @Override
    public Page<BankAccount> findPaginated(Pageable pageable) {
        return bankAccountRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Override
    public void switchAccountStatus(String accountNumber) {
        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findByAccountNumber(accountNumber);
        if (optionalBankAccount.isEmpty()) {
            throw new BankAccountNotExistException();
        }
        BankAccount bankAccount = optionalBankAccount.get();
        bankAccount.setActive(!bankAccount.isActive());

        bankAccountRepository.save(bankAccount);
    }

    @Override
    public List<BankAccount> findActiveAccountNumber(Boolean active) {
        return bankAccountRepository.findByActive(active);
    }
}
