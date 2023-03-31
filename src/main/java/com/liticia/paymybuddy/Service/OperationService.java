package com.liticia.paymybuddy.Service;

import com.liticia.paymybuddy.Entity.Operation;
import com.liticia.paymybuddy.dto.OperationCreate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OperationService {
   List<Operation> getAll();
   void saveCreditedAccount(OperationCreate operationCreate) throws Exception;
   Page<Operation> findPaginated(Pageable pageable);
}
