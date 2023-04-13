package com.liticia.paymybuddy.Service.impl;

import com.liticia.paymybuddy.Entity.Contact;
import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Repository.ContactRepository;
import com.liticia.paymybuddy.Repository.UserRepository;
import com.liticia.paymybuddy.Service.ContactService;
import com.liticia.paymybuddy.exception.ContactNotFoundException;
import com.liticia.paymybuddy.exception.NotSupportedActionException;
import com.liticia.paymybuddy.exception.UserNotFoundException;
import com.liticia.paymybuddy.security.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        if (friendUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        Optional<User> optionalUser = userRepository.findById(SecurityUtils.getCurrentUserId());
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        List<Contact> optionalContact = contactRepository.findByUserAndUserFriend(optionalUser.get(), friendUser.get());

        if (optionalContact.size() > 0) {
            throw new ContactNotFoundException();
        }

        if (optionalUser.get().equals(friendUser.get())) {
            throw new NotSupportedActionException();
        }

        Contact contact = new Contact();
        contact.setUser(optionalUser.get());
        contact.setUserFriend(friendUser.get());
        contact.setCreatedAt(new Date());

        contactRepository.save(contact);
    }

    @Override
    public Page<Contact> findPaginated(Pageable pageable) {
        return contactRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Override
    public void removeUser(long id) {
        contactRepository.deleteById(id);
    }

    @Override
    public List<Contact> getAll() {
        return contactRepository.findAll();
    }

    @Override
    public Optional<Contact> findById(long id) {
        return contactRepository.findById(id);
    }
}
