package com.liticia.paymybuddy.Service.impl;

import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Repository.BankAccountRepository;
import com.liticia.paymybuddy.Repository.UserRepository;
import com.liticia.paymybuddy.Service.BankAccountService;
import com.liticia.paymybuddy.dto.BankAccountCreate;
import com.liticia.paymybuddy.exception.BankAccountAlreadyExist;
import com.liticia.paymybuddy.exception.BankAccountNotFoundException;
import com.liticia.paymybuddy.exception.UserNotFoundException;
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
        Optional<User> optionalUser = userRepository.findById(SecurityUtils.getCurrentUserId());
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findByUserAndAccountNumber(optionalUser.get(), bankAccountCreate.getAccountNumber());
        if (optionalBankAccount.isPresent()) {
            throw new BankAccountAlreadyExist();
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
        Optional<User> currentUser = userRepository.findById(SecurityUtils.getCurrentUserId());

        return bankAccountRepository.findAllByUserOrderByCreatedAtDesc(currentUser.get(), pageable);
    }

    @Override
    public void switchAccountStatus(String accountNumber) {
        Optional<User> currentUser = userRepository.findById(SecurityUtils.getCurrentUserId());

        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findByUserAndAccountNumber(currentUser.get(), accountNumber);
        if (optionalBankAccount.isEmpty()) {
            throw new BankAccountNotFoundException();
        }
        BankAccount bankAccount = optionalBankAccount.get();
        bankAccount.setActive(!bankAccount.isActive());

        bankAccountRepository.save(bankAccount);
    }

    @Override
    public List<BankAccount> findActiveBankAccount() {
        Optional<User> currentUser = userRepository.findById(SecurityUtils.getCurrentUserId());

        return bankAccountRepository.findByUserAndActive(currentUser.get(), true);
    }
}
