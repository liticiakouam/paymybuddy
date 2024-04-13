package com.liticia.paymybuddy.Service.impl;

import com.liticia.paymybuddy.Entity.Contact;
import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Repository.ContactRepository;
import com.liticia.paymybuddy.Repository.UserRepository;
import com.liticia.paymybuddy.Service.ContactService;
import com.liticia.paymybuddy.exception.ContactAlreadyExistException;
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

        Optional<User> currentUser = userRepository.findById(SecurityUtils.getCurrentUserId());
        if (currentUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        List<Contact> optionalContact = contactRepository.findByUserAndUserFriend(currentUser.get(), friendUser.get());

        if (!optionalContact.isEmpty()) {
            throw new ContactAlreadyExistException();
        }

        if (currentUser.get().equals(friendUser.get())) {
            throw new NotSupportedActionException();
        }

        Contact contact = new Contact();
        contact.setUser(currentUser.get());
        contact.setUserFriend(friendUser.get());
        contact.setCreatedAt(new Date());
        contactRepository.save(contact);

        Contact contact1 = new Contact();
        contact1.setUser(friendUser.get());
        contact1.setUserFriend(currentUser.get());
        contact1.setCreatedAt(new Date());
        contactRepository.save(contact1);
    }

    @Override
    public Page<Contact> findPaginated(Pageable pageable) {
        Optional<User> currentUser = userRepository.findById(SecurityUtils.getCurrentUserId());
        return contactRepository.findAllByUserOrderByCreatedAtDesc(currentUser.get(), pageable);
    }

    @Override
    public void removeUser(long id) {
        contactRepository.deleteById(id);
    }

    @Override
    public List<Contact> getAll() {
        Optional<User> currentUser = userRepository.findById(SecurityUtils.getCurrentUserId());
        return contactRepository.findAllByUser(currentUser.get());
    }

    @Override
    public Optional<Contact> findById(long id) {
        return contactRepository.findById(id);
    }
}
