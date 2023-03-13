package com.liticia.paymybuddy.serviceTest;

import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Repository.BankAccountRepository;
import com.liticia.paymybuddy.Service.BankAccountService;
import com.liticia.paymybuddy.Service.impl.BankAccountServiceImpl;
import com.liticia.paymybuddy.dto.BankAccountCreate;
import com.liticia.paymybuddy.exception.BankAccountAlreadyExist;
import com.liticia.paymybuddy.exception.BankAccountNotExist;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class BankAccountServiceImplTest {

    private final BankAccountRepository bankAccountRepository = Mockito.mock(BankAccountRepository.class);
    private final BankAccountService bankAccountService = new BankAccountServiceImpl(bankAccountRepository);

    @Test
    void testShouldReturnBankAccounts() {
        List<BankAccount> list = Arrays.asList(
                BankAccount.builder().active(true).build(),
                BankAccount.builder().description("momo").build()
        ); 
        when(bankAccountRepository.findAll()).thenReturn(list);

        List<BankAccount> bankAccounts = bankAccountService.getAll();
        assertEquals(2, bankAccounts.size());
        verify(bankAccountRepository, times(1)).findAll();
    }

    @Test
    void testShouldThrowExceptionWhenBankAccountAlreadyExist() {
        BankAccount bankAccount = BankAccount.builder().accountNumber("IU13BONE").build();
        when(bankAccountRepository.findByAccountNumber("IU13BONE")).thenReturn(Optional.of(bankAccount));

        assertThrows(BankAccountAlreadyExist.class, ()->bankAccountService.save(BankAccountCreate.builder().accountNumber("IU13BONE").build()));

        verify(bankAccountRepository, times(1)).findByAccountNumber("IU13BONE");
    }

    @Test
    void testShouldSaveBankAccount() {
        BankAccountCreate bankAccountCreate = BankAccountCreate.builder().accountNumber("IU22BONE").build();

        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber(bankAccountCreate.getAccountNumber());
        when(bankAccountRepository.save(bankAccount)).thenReturn(bankAccount);

        bankAccountService.save(bankAccountCreate);
    }

    @Test
    void testShouldThrowExceptionWhenBankAccountNotExist() {
        when(bankAccountRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(BankAccountNotExist.class, ()->bankAccountService.switchAccountStatus(1));

        verify(bankAccountRepository, times(1)).findById(1);
    }

    @Test
    void testShouldChangeExistingStatusOfTheBankAccount() {
        BankAccount bankAccount = BankAccount.builder().id(1).active(true).build();
        bankAccount.setActive(!bankAccount.isActive());

        when(bankAccountRepository.findById(1)).thenReturn(Optional.of(bankAccount));
        when(bankAccountRepository.save(bankAccount)).thenReturn(bankAccount);

        bankAccountService.switchAccountStatus(1);
        verify(bankAccountRepository, times(1)).findById(1);
        verify(bankAccountRepository, times(1)).save(bankAccount);
    }

    @Test
    void testShouldReturnBankAccountsOrderByCreatedAtDesc() throws ParseException {
        Pageable pageable = PageRequest.of(1, 5);

        String dateString = "03/12/2003";
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
        String dateSg = "03/12/2010";
        Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(dateSg);
        List<BankAccount> bankAccounts = Arrays.asList(
                BankAccount.builder().description("mtn").createdAt(date2).build(),
                BankAccount.builder().description("momo").createdAt(date1).build()
        );
        Page<BankAccount> bankAccount = new PageImpl<>(bankAccounts);

        when(bankAccountRepository.findAllByOrderByCreatedAtDesc(pageable)).thenReturn(bankAccount);

        Page<BankAccount> paginated = bankAccountService.findPaginated(pageable);

        assertEquals(2, paginated.getTotalElements());
        assertEquals("mtn", paginated.getContent().get(0).getDescription());
        verify(bankAccountRepository, times(1)).findAllByOrderByCreatedAtDesc(pageable);
    }
}
