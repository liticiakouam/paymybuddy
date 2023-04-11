package com.liticia.paymybuddy.Service;

import com.liticia.paymybuddy.Entity.Contact;
import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.dto.ContactCreated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContactService {
    void save(long friendId);
    Page<Contact> findPaginated(Pageable pageable);
    void removeUser(User user);
}
