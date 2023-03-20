package com.liticia.paymybuddy.Repository;

import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
