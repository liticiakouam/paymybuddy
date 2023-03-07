package com.liticia.paymybuddy.Service.impl;

import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Repository.BankAccountRepository;
import com.liticia.paymybuddy.Service.BankAccountService;
import com.liticia.paymybuddy.dto.BankAccountCreate;
import com.liticia.paymybuddy.exception.AccountNumberAlreadyExist;
import com.liticia.paymybuddy.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BankAccountServiceImpl implements BankAccountService {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Override
    public List<BankAccount> getAll() {
        return bankAccountRepository.findAll();
    }

    @Override
    public void save(BankAccountCreate bankAccountCreate) {
        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findByAccountNumber(bankAccountCreate.getAccountNumber());
        if (optionalBankAccount.isPresent()) {
            throw new AccountNumberAlreadyExist();
        }

        BankAccount bankAccount = new BankAccount();
        bankAccount.setDescription(bankAccountCreate.getDescription());
        bankAccount.setAccountNumber(bankAccountCreate.getAccountNumber());
        bankAccount.setCreatedAt(new Date());
        bankAccount.setUserId(SecurityUtils.getCurrentUserId());

        bankAccountRepository.save(bankAccount);
    }

    @Override
    public Page<BankAccount> findPaginated(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        return bankAccountRepository.findAll(pageable);
    }

    @Override
    public void switchAccountStatus(int id) {
        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findById(id);
        //TODO: handle empty optionalBankAccount
        if (optionalBankAccount.isEmpty()) {
            return;
        }
        BankAccount bankAccount = optionalBankAccount.get();
        bankAccount.setActive(!bankAccount.isActive());

        bankAccountRepository.save(bankAccount);
    }
}
