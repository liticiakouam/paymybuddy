package com.liticia.paymybuddy.Repository;

import com.liticia.paymybuddy.Entity.Operation;
import com.liticia.paymybuddy.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    Page<Operation> findAllByUserOrderByOperationDateDesc(User user, Pageable pageable);
}
