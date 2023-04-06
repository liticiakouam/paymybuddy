package com.liticia.paymybuddy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Entity.Operation;
import com.liticia.paymybuddy.Entity.OperationType;
import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Service.BankAccountService;
import com.liticia.paymybuddy.Service.OperationService;
import com.liticia.paymybuddy.Service.UserService;
import com.liticia.paymybuddy.dto.OperationCreate;
import com.liticia.paymybuddy.exception.InsufficientBalanceException;
import com.liticia.paymybuddy.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {UserController.class})
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @Test
    public void testShouldVerifyThatControllerReturnOkStatusAndUserLengthIsCorrect() throws Exception {
        List<User> users = Arrays.asList(
                User.builder().firstname("liticia").lastname("anz").balance(1000.0).build(),
                User.builder().firstname("abena").lastname("anze").balance(1000.0).build()
        );

        when(userService.getAll()).thenReturn(users);

         mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("user"))
                .andReturn();

    }

    @Test
    public void testShouldVerifyThatControlleturnOkStatusAndUserLengthIsCorrect() throws Exception {
        List<User> users = Arrays.asList(
                User.builder().firstname("liticia").lastname("anz").balance(1000.0).build(),
                User.builder().firstname("abena").lastname("anze").balance(1000.0).build()
        );

        when(userService.search("liti")).thenReturn(users);

         mockMvc.perform(get("/users/search?keyword=liti"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("userSize"))
                .andExpect(model().attributeExists("searchUsers"))
                .andReturn();

    }


}
