package com.liticia.paymybuddy.Service.impl;

import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Repository.UserRepository;
import com.liticia.paymybuddy.Service.UserService;
import com.liticia.paymybuddy.dto.UserDto;
import com.liticia.paymybuddy.exception.UserAlreadyExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public void saveUser(UserDto userDto) {
        User existingUser = userRepository.findUserByEmail(userDto.getEmail());
        if (existingUser != null) {
            throw new UserAlreadyExistException();
        }
        User user = new User();

        user.setLastname(userDto.getLastname());
        user.setFirstname(userDto.getFirstname());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        userRepository.save(user);
    }

    @Override
    public User editUserInfo(long id) {
        User user = findById(id).get();

        user.setFirstname(user.getFirstname());
        user.setLastname(user.getLastname());
        user.setEmail(user.getEmail());

        return userRepository.save(user);
    }
}
