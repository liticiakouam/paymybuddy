package com.liticia.paymybuddy.Service.impl;

import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Repository.UserRepository;
import com.liticia.paymybuddy.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> search(String keyword) {
        return userRepository.findByFirstnameContainingOrLastnameContaining(keyword, keyword);
    }
}
