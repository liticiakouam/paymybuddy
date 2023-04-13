package com.liticia.paymybuddy.controller;

import com.liticia.paymybuddy.Entity.*;
import com.liticia.paymybuddy.Service.ContactService;
import com.liticia.paymybuddy.Service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {TransactionController.class})
public class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private ContactService contactService;

    @Test
    public void testShouldVerifyThatControllerReturnOkStatusAndTransactionLengthIsCorrect() throws Exception {
        List<Transaction> transactions = Arrays.asList(
                Transaction.builder().amount(2000).id(2).build(),
                Transaction.builder().amount(5000).id(3).build()
        );

        User userF = User.builder().id(1).firstname("anze").build();
        User user = User.builder().id(2).firstname("anz").build();

        Contact contact = Contact.builder().id(1).user(user).userFriend(userF).build();
        Page<Transaction> page = new PageImpl<>(transactions);

        when(transactionService.findAll(any(Pageable.class))).thenReturn(page);
        when(contactService.findById(1)).thenReturn(Optional.of(contact));

         mockMvc.perform(get("/transaction/contact?contactId=1"))
                .andExpect(status().isFound())
                .andExpect(view().name("transaction"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("totalItems"))
                .andExpect(model().attributeExists("contact"))
                .andExpect(model().attributeExists("transaction"))
                .andExpect(model().attributeExists("transactions"))
                .andReturn();

    }

}
