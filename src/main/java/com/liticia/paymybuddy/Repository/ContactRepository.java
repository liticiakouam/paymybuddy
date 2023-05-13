package com.liticia.paymybuddy.Repository;

import com.liticia.paymybuddy.Entity.Contact;
import com.liticia.paymybuddy.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByUserAndUserFriend(User user, User userFriend);
    List<Contact> findAllByUser(User user);
    Page<Contact> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
