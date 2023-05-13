package com.liticia.paymybuddy.controller;

import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {UserController.class},
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testShouldVerifyThatControllerReturnOkStatusAndUserLengthIsCorrect() throws Exception {
        List<User> users = Arrays.asList(
                User.builder().firstname("liticia").lastname("anz").balance(1000).build(),
                User.builder().firstname("abena").lastname("anze").balance(1000).build()
        );

        Pageable pageable = PageRequest.of(1, 5);
        Page<User> page = new PageImpl<>(users);

        when(userService.findPaginated(pageable)).thenReturn(page);

        mockMvc.perform(get("/user?pageNumber=2"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("totalItems"))
                .andExpect(model().attributeExists("users"))
                .andReturn();

    }

    @Test
    public void testShouldVerifyThatControllerReturnOkStatusAndSeachUserResult() throws Exception {
        List<User> users = Arrays.asList(
                User.builder().firstname("liticia").lastname("anz").balance(1000).build(),
                User.builder().firstname("abena").lastname("anze").balance(1000).build()
        );

        when(userService.search("liti")).thenReturn(users);

         mockMvc.perform(get("/user/search?keyword=liti"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("userSize"))
                .andExpect(model().attributeExists("searchUsers"))
                .andReturn();

    }


}
