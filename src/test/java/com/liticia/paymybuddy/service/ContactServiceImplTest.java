package com.liticia.paymybuddy.service;

import com.liticia.paymybuddy.Entity.Contact;
import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Repository.ContactRepository;
import com.liticia.paymybuddy.Repository.UserRepository;
import com.liticia.paymybuddy.Service.ContactService;
import com.liticia.paymybuddy.Service.UserService;
import com.liticia.paymybuddy.Service.impl.ContactServiceImpl;
import com.liticia.paymybuddy.Service.impl.UserServiceImpl;
import com.liticia.paymybuddy.exception.InsufficientBalanceException;
import com.liticia.paymybuddy.exception.UserAlreadyExistException;
import com.liticia.paymybuddy.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ContactServiceImplTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final ContactRepository contactRepository = Mockito.mock(ContactRepository.class);

    private final ContactService contactService = new ContactServiceImpl(contactRepository, userRepository);

    @Test
    void testShouldAddAUserIntoYourContact() {
        User userF = User.builder().id(1).firstname("anze").build();
        User user = User.builder().id(2).firstname("anz").build();

        List<Contact> contacts = Arrays.asList(
                Contact.builder().user(user).userFriend(userF).build(),
                Contact.builder().user(user).userFriend(userF).build()
        );

        when(userRepository.findById(2L)).thenReturn(Optional.of(userF));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(contactRepository.findByUserFriend(user)).thenReturn(contacts);

        contactService.save(2);
        verify(userRepository, times(2)).findById(any());
        verify(contactRepository, times(1)).findByUserFriend(any(User.class));

    }

    @Test
    void testShouldThrowUserAlreadyExistExceptionWhenTheUserYouWantToAddIsAlreadyYourFriend() {
        User userF = User.builder().id(1).firstname("anze").build();
        User user = User.builder().id(2).firstname("anze").build();

        List<Contact> contacts = Arrays.asList(
                Contact.builder().user(user).userFriend(userF).build(),
                Contact.builder().user(user).userFriend(userF).build()
        );

        when(userRepository.findById(2L)).thenReturn(Optional.of(userF));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(contactRepository.findByUserFriend(user)).thenReturn(contacts);
        assertThrows(UserAlreadyExistException.class, ()->contactService.save(userF.getId()));

        verify(userRepository, times(2)).findById(any());
        verify(contactRepository, times(1)).findByUserFriend(any(User.class));
    }

}
