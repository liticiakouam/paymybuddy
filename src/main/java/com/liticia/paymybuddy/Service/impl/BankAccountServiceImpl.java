package com.liticia.paymybuddy.Service.impl;

import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Repository.BankAccountRepository;
import com.liticia.paymybuddy.Service.BankAccountService;
import com.liticia.paymybuddy.dto.BankAccountCreate;
import com.liticia.paymybuddy.exception.BankAccountAlreadyExist;
import com.liticia.paymybuddy.exception.BankAccountNotExist;
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

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public List<BankAccount> getAll() {
        return bankAccountRepository.findAll();
    }

    @Override
    public void save(BankAccountCreate bankAccountCreate) {
        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findByAccountNumber(bankAccountCreate.getAccountNumber());
        if (optionalBankAccount.isPresent()) {
            throw new BankAccountAlreadyExist();
        }

        BankAccount bankAccount = new BankAccount();
        bankAccount.setDescription(bankAccountCreate.getDescription());
        bankAccount.setAccountNumber(bankAccountCreate.getAccountNumber());
        bankAccount.setCreatedAt(new Date());
        bankAccount.setUserId(SecurityUtils.getCurrentUserId());

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
            throw new BankAccountNotExist();
        }
        BankAccount bankAccount = optionalBankAccount.get();
        bankAccount.setActive(!bankAccount.isActive());

        bankAccountRepository.save(bankAccount);
    }

    @Override
    public List<BankAccount> findActiveAccountNumber(Boolean active) {
        List<BankAccount> accountList = bankAccountRepository.findByActive(active);
        return accountList;
    }
}
