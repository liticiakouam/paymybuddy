package com.liticia.paymybuddy.Service;

import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.dto.BankAccountCreate;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BankAccountService {
    List<BankAccount> getAll() ;

    void save(BankAccountCreate bankAccountCreate);

    Page<BankAccount> findPaginated(int pageNo, int pageSize);

    void switchAccountStatus(int id);
}
