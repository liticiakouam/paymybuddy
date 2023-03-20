package com.liticia.paymybuddy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Service.BankAccountService;
import com.liticia.paymybuddy.dto.BankAccountCreate;
import com.liticia.paymybuddy.exception.BankAccountAlreadyExist;
import com.liticia.paymybuddy.exception.BankAccountNotExist;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {BankAccountController.class})
public class BankAccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankAccountService bankAccountService;

    @Test
    public void testShouldVerifyThatControllerReturnOkStatusAndBankAccountLengthIsCorrect() throws Exception {
        List<BankAccount> bankAccounts = Arrays.asList(
                BankAccount.builder().description("mtn").build(),
                BankAccount.builder().description("momo").build()
        );
        Pageable pageable = PageRequest.of(1, 5);
        Page<BankAccount> page = new PageImpl<>(bankAccounts);

        when(bankAccountService.findPaginated(pageable)).thenReturn(page);

         mockMvc.perform(get("/bankAccount?pageNumber=2"))
                .andExpect(status().isOk())
                .andExpect(view().name("bankAccount"))
                .andExpect(model().attributeExists("bankAccount"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("totalItems"))
                .andExpect(model().attributeExists("bankAccounts"))
                .andReturn();

    }

    @Test
    public void testShouldReturnOkWhenCreatedBankAccount() throws Exception {
        BankAccountCreate bankAccountCreate = BankAccountCreate.builder().accountNumber("IU12UBA").description("UBA").build();
        List<BankAccount> bankAccounts = Arrays.asList(
                BankAccount.builder().description("mtn").build(),
                BankAccount.builder().description("momo").build()
        );
        Pageable pageable = PageRequest.of(1, 5);
        Page<BankAccount> page = new PageImpl<>(bankAccounts);

        when(bankAccountService.findPaginated(pageable)).thenReturn(page);
        doNothing().when(bankAccountService).save(bankAccountCreate);

        String content = new ObjectMapper().writeValueAsString(bankAccountCreate);
        MockHttpServletRequestBuilder mockRequest = post("/bankAccount/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void testShouldReturnOkWhenExceptionThrow() throws Exception {
        BankAccountCreate bankAccountCreate = BankAccountCreate.builder().accountNumber("IU12UBA").description("UBA").build();
        List<BankAccount> bankAccounts = Arrays.asList(
                BankAccount.builder().description("mtn").build(),
                BankAccount.builder().description("momo").build()
        );
        Pageable pageable = PageRequest.of(1, 5);
        Page<BankAccount> page = new PageImpl<>(bankAccounts);

        doThrow(BankAccountAlreadyExist.class).when(bankAccountService).save(bankAccountCreate);
        when(bankAccountService.findPaginated(pageable)).thenReturn(page);

        String content = new ObjectMapper().writeValueAsString("IU12UBA");
        MockHttpServletRequestBuilder mockRequest = post("/bankAccount/add")
                .content(content);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void testShouldReturnAnswerWhenEmptyValue() throws Exception {
        BankAccountCreate bankAccountCreate = BankAccountCreate.builder().accountNumber("").description("UBA").build();
        List<BankAccount> bankAccounts = Arrays.asList(
                BankAccount.builder().description("mtn").build(),
                BankAccount.builder().description("momo").build()
        );
        Pageable pageable = PageRequest.of(1, 5);
        Page<BankAccount> page = new PageImpl<>(bankAccounts);

        when(bankAccountService.findPaginated(pageable)).thenReturn(page);
        doAnswer(invocation -> "please fill the value").when(bankAccountService).save(bankAccountCreate);

        String content = new ObjectMapper().writeValueAsString(bankAccountCreate);
        MockHttpServletRequestBuilder mockRequest = post("/bankAccount/add")
                .content(content);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void testShouldReturnFoundStatusWhenDisableBankAccount() throws Exception {
        doNothing().when(bankAccountService).switchAccountStatus(21);

        mockMvc.perform(get("/disable/21"))
                .andExpect(status().isFound());
    }

}
