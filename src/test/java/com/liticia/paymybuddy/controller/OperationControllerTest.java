package com.liticia.paymybuddy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Entity.Operation;
import com.liticia.paymybuddy.Entity.OperationType;
import com.liticia.paymybuddy.Service.BankAccountService;
import com.liticia.paymybuddy.Service.OperationService;
import com.liticia.paymybuddy.dto.BankAccountCreate;
import com.liticia.paymybuddy.dto.OperationCreate;
import com.liticia.paymybuddy.exception.BankAccountAlreadyExist;
import org.junit.jupiter.api.Test;
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

@WebMvcTest(controllers = {OperationController.class})
public class OperationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankAccountService bankAccountService;

    @MockBean
    private OperationService operationService;

    @Test
    public void testShouldVerifyThatControllerReturnOkStatusAndOperationLengthIsCorrect() throws Exception {
        List<Operation> operations = Arrays.asList(
                Operation.builder().id(2).bankAccount(BankAccount.builder().description("MOMO").build()).operationType(OperationType.DEBIT).build(),
                Operation.builder().id(1).bankAccount(BankAccount.builder().description("MOMO").build()).amount(2000.0).build()
        );
        List<BankAccount> bankAccounts = Arrays.asList(
                BankAccount.builder().description("mtn").build(),
                BankAccount.builder().description("momo").build()
        );
        Page<Operation> page = new PageImpl<>(operations);

        when(operationService.findPaginated(any(Pageable.class))).thenReturn(page);
        when(bankAccountService.findActiveAccountNumber(true)).thenReturn(bankAccounts);

         mockMvc.perform(get("/operation?pageNumber=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("operation"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("totalItems"))
                 .andExpect(model().attributeExists("operations"))
                .andExpect(model().attributeExists("bankAccounts"))
                .andReturn();

    }

    @Test
    public void testShouldReturnOkWhenCreatedOperation() throws Exception {
        OperationCreate operationCreate = OperationCreate.builder().accountNumber("IU12UBA").operationType(OperationType.CREDIT).amount(11000.0).build();

        doNothing().when(operationService).save(operationCreate);

        String content = new ObjectMapper().writeValueAsString(operationCreate);
        MockHttpServletRequestBuilder mockRequest = post("/operation/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(mockRequest)
                .andExpect(status().isFound());
    }

}
