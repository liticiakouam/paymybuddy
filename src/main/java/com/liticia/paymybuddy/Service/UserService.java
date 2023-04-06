package com.liticia.paymybuddy.Service;

import com.liticia.paymybuddy.Entity.User;

import java.util.List;

public interface UserService {
    public void save(User user);
    List<User> getAll();
    List<User> searchByFirstnameOrLastname(String firstname, String lastname);

}
