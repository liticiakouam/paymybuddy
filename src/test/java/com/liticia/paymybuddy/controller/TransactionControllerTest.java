package com.liticia.paymybuddy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liticia.paymybuddy.Entity.*;
import com.liticia.paymybuddy.Service.ContactService;
import com.liticia.paymybuddy.Service.TransactionService;
import com.liticia.paymybuddy.dto.OperationCreate;
import com.liticia.paymybuddy.dto.TransactionCreate;
import com.liticia.paymybuddy.exception.ContactNotFoundException;
import com.liticia.paymybuddy.exception.InsufficientBalanceException;
import com.liticia.paymybuddy.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {TransactionController.class},
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private ContactService contactService;

    @Test
    public void testShouldReturnOkWhenCreatedTransaction() throws Exception {
        TransactionCreate transactionCreate = TransactionCreate.builder().contactId(1).amount(5000).build();

        doNothing().when(transactionService).save(transactionCreate);

        String content = new ObjectMapper().writeValueAsString(transactionCreate);
        MockHttpServletRequestBuilder mockRequest = post("/transaction/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(mockRequest)
                .andExpect(status().isFound());
    }

    @Test
    public void testShouldThrowContactNotFoundException() throws Exception {
        TransactionCreate transactionCreate = TransactionCreate.builder().contactId(1).amount(5000).build();

        doThrow(ContactNotFoundException.class).when(transactionService).save(transactionCreate);

        String content = new ObjectMapper().writeValueAsString(transactionCreate);
        MockHttpServletRequestBuilder mockRequest = post("/transaction/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(mockRequest)
                .andExpect(status().isFound());
    }

    @Test
    public void testShouldThrowInsufficientBalanceException() throws Exception {
        TransactionCreate transactionCreate = TransactionCreate.builder().contactId(1).amount(5000).build();

        doThrow(InsufficientBalanceException.class).when(transactionService).save(transactionCreate);

        String content = new ObjectMapper().writeValueAsString(transactionCreate);
        MockHttpServletRequestBuilder mockRequest = post("/transaction/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(mockRequest)
                .andExpect(status().isFound());
    }

}
