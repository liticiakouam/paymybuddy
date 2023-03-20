package com.liticia.paymybuddy.Service;

import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.dto.BankAccountCreate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface BankAccountService {
    List<BankAccount> getAll();

    void save(BankAccountCreate bankAccountCreate);

    Page<BankAccount> findPaginated(Pageable pageable);

    void switchAccountStatus(int id);
}
