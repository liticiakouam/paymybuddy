package com.liticia.paymybuddy.repository;

import com.liticia.paymybuddy.Entity.Contact;
import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Repository.ContactRepository;
import com.liticia.paymybuddy.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/paymybuddytest?createDatabaseIfExist=true&useSSL=false&serverTimezone=UTC\n"
})
public class ContactRepositoryTest {
    @Autowired
    private ContactRepository contactRepository;

    @Test
    void testShouldFindUserByFriend() {
        List<Contact> userFriend = contactRepository.findByUserFriend(User.builder().id(1).build());

        assertEquals(2, userFriend.size());
        assertEquals("liti@gmail.com", userFriend.get(0).getUser().getEmail());
    }
    @Test
    void testShouldDeleteContact() {
        contactRepository.deleteById(1l);

    }

}
