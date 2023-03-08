package com.liticia.paymybuddy.serviceTest;

import com.liticia.paymybuddy.Repository.BankAccountRepository;
import com.liticia.paymybuddy.Service.impl.BankAccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.client.ExpectedCount.times;

public class BankAccountServiceImplTest {
    @Autowired
    MockMvc mockMvc;

 /*   @MockBean
    BankAccountRepository bankAccountRepository;
    BankAccountServiceImpl bankAccountService = new BankAccountServiceImpl(bankAccountRepository);

    @Test
    public void testShouldVerifyTheNumberOfTimeThatBankAccountRepositoryIsCallIntoBankAccountService() {
        bankAccountService.getAll();
        //Mockito.verify(bankAccountRepository, times(1)).findAll();
    }*/
}
