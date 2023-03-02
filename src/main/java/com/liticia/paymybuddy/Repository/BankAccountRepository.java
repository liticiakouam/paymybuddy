package com.liticia.paymybuddy.Repository;

import com.liticia.paymybuddy.Entity.BankAccount;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {

    @Query("UPDATE BankAccount ba SET status = 0 WHERE ba.id LIKE :id")
    public BankAccount updateByStatus(@RequestParam(value = "id") int id);
}
