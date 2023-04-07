package com.liticia.paymybuddy.Service;

import com.liticia.paymybuddy.Entity.Contact;
import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.dto.ContactCreated;

import java.util.List;

public interface ContactService {
    void save(ContactCreated contactCreated);
}
