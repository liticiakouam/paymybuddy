package com.liticia.paymybuddy.Service;

import com.liticia.paymybuddy.Entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ContactService {
    void save(long friendId);
    Page<Contact> findPaginated(Pageable pageable);
    void removeUser(long id);
    List<Contact> getAll();

    Optional<Contact> findById(long id);
}
