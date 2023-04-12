package com.liticia.paymybuddy.Service;

import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Entity.Operation;
import com.liticia.paymybuddy.Entity.OperationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OperationService {
    List<Operation> getAll();

    void creditAccount(float amount, String accountNumber);

    void debitAccount(float amount, String accountNumber);

    Page<Operation> findPaginated(Pageable pageable);
}
