package com.liticia.paymybuddy.Service;

import com.liticia.paymybuddy.Entity.Transaction;
import com.liticia.paymybuddy.dto.TransactionCreate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {
    List<Transaction> getAll();
    Page<Transaction> findAll(Pageable pageable);
    void save(TransactionCreate transactionCreate);
}
