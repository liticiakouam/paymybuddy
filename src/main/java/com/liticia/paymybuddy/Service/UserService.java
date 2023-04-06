package com.liticia.paymybuddy.Service;

import com.liticia.paymybuddy.Entity.User;

import java.util.List;

public interface UserService {
    List<User> getAll();
    List<User> search(String keyword);
}
