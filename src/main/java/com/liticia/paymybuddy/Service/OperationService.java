package com.liticia.paymybuddy.Service;

import com.liticia.paymybuddy.Entity.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OperationService {
    List<Operation> getAll();

    void creditAccount(float amount, String accountNumber);

    void debitAccount(float amount, String accountNumber);

    Page<Operation> findAll(Pageable pageable);
}
