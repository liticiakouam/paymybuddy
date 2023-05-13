package com.liticia.paymybuddy.Repository;

import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
    Optional<BankAccount> findByUserAndAccountNumber(User user, String accountNumber);
    List<BankAccount> findByUserAndActive(User user, Boolean active);
    Page<BankAccount> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
