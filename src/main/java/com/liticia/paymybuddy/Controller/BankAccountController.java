package com.liticia.paymybuddy.Controller;

import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Service.BankAccountService;
import com.liticia.paymybuddy.dto.BankAccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @GetMapping("/bankAccount")
    public String getBankAccount (Model model) {
        model.addAttribute("bankAccounts", bankAccountService.getAll());
        return "account";
    }

    @GetMapping("/bankAccount/add")
    public String showBankAccount (Model model) {
        BankAccount bankAccount = new BankAccount();
        model.addAttribute("bankAccount", bankAccount);
        return "new";
    }

    @PostMapping("/bankAccount/add")
    public String postBankAccount (@ModelAttribute("bankAccount") BankAccount bankAccount) {
        bankAccountService.save(bankAccount);
        return "redirect:/bankAccount";
    }

    @GetMapping("/disable/{id}")
    public String updateBankAccount(@PathVariable int id) {
        BankAccount bankAccount = bankAccountService.update(id);
        return "redirect:/bankAccount";
    }
}
