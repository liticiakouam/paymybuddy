package com.liticia.paymybuddy.Service;

import com.liticia.paymybuddy.Entity.Operation;
import com.liticia.paymybuddy.dto.OperationCreate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OperationService {
    List<Operation> getAll();
    void saveCreditedAccount(OperationCreate operationCreate);

    @Transactional
    void saveDebitedAccount(OperationCreate operationCreate);

    Page<Operation> findPaginated(Pageable pageable);
}
