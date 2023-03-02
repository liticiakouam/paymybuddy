package com.liticia.paymybuddy.Service;

import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.dto.BankAccountDTO;

import java.util.List;

public interface BankAccountService {
    public List<BankAccount> getAll() ;
    public void save(BankAccount bankAccount);

    public BankAccount update (int id);

}
