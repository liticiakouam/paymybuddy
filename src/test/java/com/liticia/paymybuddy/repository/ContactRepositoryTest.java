package com.liticia.paymybuddy.repository;

import com.liticia.paymybuddy.Entity.Contact;
import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Repository.ContactRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/paymybuddytest?createDatabaseIfExist=true&useSSL=false&serverTimezone=UTC\n"
})
public class ContactRepositoryTest {
    @Autowired
    private ContactRepository contactRepository;

    @Test
    void testShouldFindContactByPrincipalAndUserFriend() {

        User userFriend = User.builder().id(1).firstname("anze").build();
        User user = User.builder().id(1).firstname("anze").build();
        List<Contact> contacts = contactRepository.findByUserAndUserFriend(user, userFriend);

        assertEquals(0, contacts.size());
    }
    @Test
    void testShouldDeleteContact() {
        contactRepository.deleteById(1L);
        Optional<Contact> optionalContact = contactRepository.findById(1L);
        assertTrue(optionalContact.isEmpty());
    }

}
