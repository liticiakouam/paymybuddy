package com.liticia.paymybuddy.Repository;

import com.liticia.paymybuddy.Entity.Contact;
import com.liticia.paymybuddy.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByUserFriend(User user);
    Page<Contact> findAllByOrderByCreatedAtDesc(Pageable pageable);
 /*   @Query(value = "SELECT firstname, lastname FROM contact, user WHERE contact.friend_user = user.id AND user.firstname LIKE '%:firstname%' OR user.lastname LIKE '%:lastname%'")
    List<Contact> search (@Param(value = "firstname") String firstname, @Param(value = "lastname")String lastname);*/
}
