package com.liticia.paymybuddy.service;

import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Repository.RoleRepository;
import com.liticia.paymybuddy.Repository.UserRepository;
import com.liticia.paymybuddy.Service.UserService;
import com.liticia.paymybuddy.Service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
    private final PasswordEncoder bCryptPasswordEncoder = Mockito.mock(PasswordEncoder.class);
    private final UserService userService = new UserServiceImpl(userRepository, roleRepository, bCryptPasswordEncoder);

    @Test
    void testShouldReturnUsers() {
        List<User> users = Arrays.asList(
                User.builder().firstname("liticia").lastname("anzwe").balance(1000).build(),
                User.builder().firstname("momo").build()
        ); 
        when(userRepository.findAll()).thenReturn(users);

        List<User> userList = userService.getAll();
        assertEquals(2, userList.size());
        assertEquals(1000.0, userList.get(0).getBalance());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testSholdReturnUsers() {
        List<User> users = Collections.singletonList(
                User.builder().firstname("liticia").lastname("anzwe").balance(1000).build()
        );
        when(userRepository.findByFirstnameContainingOrLastnameContaining("liti", "liti")).thenReturn(users);

        List<User> searchUser = userService.search("liti");
        assertEquals(1, searchUser.size());
        assertEquals(1000.0, searchUser.get(0).getBalance());
        verify(userRepository, times(1)).findByFirstnameContainingOrLastnameContaining("liti", "liti");
    }
}
