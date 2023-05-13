package com.liticia.paymybuddy.service;

import com.liticia.paymybuddy.Entity.Contact;
import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Repository.ContactRepository;
import com.liticia.paymybuddy.Repository.UserRepository;
import com.liticia.paymybuddy.Service.ContactService;
import com.liticia.paymybuddy.Service.impl.ContactServiceImpl;
import com.liticia.paymybuddy.exception.NotSupportedActionException;
import com.liticia.paymybuddy.security.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ContactServiceImplTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    MockedStatic<SecurityUtils> securityUtils = Mockito.mockStatic(SecurityUtils.class);
    private final ContactRepository contactRepository = Mockito.mock(ContactRepository.class);
    private final ContactService contactService = new ContactServiceImpl(contactRepository, userRepository);

    @Test
    void testShouldAddAUserIntoYourContact() {
        User userFriend = User.builder().id(1).firstname("anze").build();
        User user = User.builder().id(2).firstname("anz").build();

        List<Contact> contacts = Arrays.asList(
                Contact.builder().user(user).userFriend(userFriend).build(),
                Contact.builder().user(user).userFriend(userFriend).build()
        );

        securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(2L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(userFriend));
        when(contactRepository.findByUserAndUserFriend(user, userFriend)).thenReturn(contacts);

        contactService.save(1L);
        verify(userRepository, times(2)).findById(any());
        verify(contactRepository, times(1)).findByUserAndUserFriend(any(User.class), any(User.class));

    }

    @Test
    void testShouldThrowNotSupportedOperationExceptionWhenTheActionCannotBeSupported() {
        User userFriend = User.builder().id(1).firstname("anze").build();
        User user = User.builder().id(2).firstname("anze").build();

        List<Contact> contacts = Arrays.asList(
                Contact.builder().user(user).userFriend(userFriend).build(),
                Contact.builder().user(user).userFriend(userFriend).build()
        );

        securityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(2L);
        when(userRepository.findById(2L)).thenReturn(Optional.of(userFriend));
        when(contactRepository.findByUserAndUserFriend(user, userFriend)).thenReturn(contacts);
        assertThrows(NotSupportedActionException.class, ()->contactService.save(2));

        verify(userRepository, times(2)).findById(any());
        verify(contactRepository, times(1)).findByUserAndUserFriend( any(User.class), any(User.class));
    }

    @Test
    void testShouldDeleteContact() {
        doNothing().when(contactRepository).deleteById(1L);
        contactService.removeUser(1);

        Optional<Contact> optionalContact = contactService.findById(1);
        assertTrue(optionalContact.isEmpty());
        verify(contactRepository, times(1)).deleteById(1L);
    }


    @Test
    void testShouldFindContactById() {
        Contact contact = Contact.builder().id(1).build();
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));

        Optional<Contact> optionalContact = contactService.findById(1);
        assertTrue(optionalContact.isPresent());
        verify(contactRepository, times(1)).findById(1L);
    }

}
