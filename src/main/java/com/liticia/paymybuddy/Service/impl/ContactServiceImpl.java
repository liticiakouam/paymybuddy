package com.liticia.paymybuddy.Service.impl;

import com.liticia.paymybuddy.Entity.Contact;
import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Repository.ContactRepository;
import com.liticia.paymybuddy.Repository.UserRepository;
import com.liticia.paymybuddy.Service.ContactService;
import com.liticia.paymybuddy.dto.ContactCreated;
import com.liticia.paymybuddy.exception.UserAlreadyExistException;
import com.liticia.paymybuddy.exception.UserNotFoundException;
import com.liticia.paymybuddy.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    public ContactServiceImpl(ContactRepository contactRepository, UserRepository userRepository) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void save(long friendId) {
        Optional<User> friendUser = userRepository.findById(friendId);
        Optional<User> optionalUser = userRepository.findById(SecurityUtils.getCurrentUserId());
        List<Contact> optionalContact = contactRepository.findByUserFriend(friendUser.get());

        if (optionalContact.size() > 0) {
            throw new UserAlreadyExistException();
        }
        if (friendUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        Contact contact = new Contact();
        contact.setUser(optionalUser.get());
        contact.setUserFriend(friendUser.get());
        contact.setCreatedAt(new Date());

        contactRepository.save(contact);
    }
}
