package com.liticia.paymybuddy.controllerTest;

import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Service.BankAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = {BankAccount.class})
public class BankAccountController {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankAccountService bankAccountService;

/*    @Test
    public void testShouldVerifyThatControllerReturnOkStatusAndPerssonLengthIsCorrect() throws Exception {
        List<BankAccount> bankAccounts = Arrays.asList(
                BankAccount.builder().active(true).build(),
                BankAccount.builder().description("momo").build()
        );
        when(bankAccountService.findPaginated()).thenReturn(bankAccounts);

        MvcResult result = mockMvc.perform(get("/bankAccount?pageNo=1"))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        MedicalRecord[] fireStationRetrieved = new ObjectMapper().readValue(contentAsString, MedicalRecord[].class);

        assertEquals(2, fireStationRetrieved.length);
        verify(medicalRecordService, times(1)).findAll();
    }*/
}
