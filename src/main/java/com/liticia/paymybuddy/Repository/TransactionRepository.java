package com.liticia.paymybuddy.Repository;

import com.liticia.paymybuddy.Entity.Transaction;
import com.liticia.paymybuddy.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findAllByUserOrderByTransactionDateDesc(User user, Pageable pageable);
}
