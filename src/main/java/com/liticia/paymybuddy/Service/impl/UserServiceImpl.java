package com.liticia.paymybuddy.Service.impl;

import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Repository.UserRepository;
import com.liticia.paymybuddy.Service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Page<User> findPaginated(Pageable pageable) {
        return userRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }
}
