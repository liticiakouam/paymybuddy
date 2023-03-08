package com.liticia.paymybuddy.Repository;

import com.liticia.paymybuddy.Entity.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
    Optional<BankAccount> findByAccountNumber(String accountNumber);
     Page<BankAccount> findAllByOrderByCreatedAtDesc(Pageable pageable);

}
