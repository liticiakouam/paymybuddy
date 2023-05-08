package com.liticia.paymybuddy.Repository;

import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByFirstnameContainingOrLastnameContaining(String firstname, String lastname);

    Page<User> findAllByOrderByCreatedAtDesc(Pageable pageable);

    User findUserByEmail(String email);
}
