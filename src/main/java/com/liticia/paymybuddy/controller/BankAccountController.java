package com.liticia.paymybuddy.controller;

import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Service.BankAccountService;
import com.liticia.paymybuddy.dto.BankAccountCreate;
import com.liticia.paymybuddy.exception.BankAccountAlreadyExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @GetMapping("/bankAccount")//http://localhost:8080/bankAccount?pageNo=1
    public String getBankAccount(
            @RequestParam("pageNumber") int pageNumber,
            Model model
    ) {

        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        Page <BankAccount> page = bankAccountService.findPaginated(pageable);
        List <BankAccount> bankAccounts = page.getContent();
        BankAccountCreate bankAccount = new BankAccountCreate();

        model.addAttribute("bankAccount", bankAccount);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("bankAccounts", bankAccounts);

        return "bankAccount";
    }

    @PostMapping("/bankAccount/add")
    public String postBankAccount(@Valid @ModelAttribute("bankAccount") BankAccountCreate bankAccountCreate,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("bankAccounts", bankAccountService.getAll());
            return "bankAccount";

        } try {
           bankAccountService.save(bankAccountCreate);
           redirectAttributes.addFlashAttribute("saved", "The bankAccount is save!");

        } catch (BankAccountAlreadyExist e) {
            redirectAttributes.addFlashAttribute("message", "Sorry, a bank account with the account number already exist");
            model.addAttribute("bankAccounts", bankAccountService.getAll());

            return "redirect:/bankAccount?pageNumber=1";
        }

        return "redirect:/bankAccount?pageNumber=1";
    }

    @GetMapping("/disable/{id}")
    public String updateBankAccount(@PathVariable int id) {
        bankAccountService.switchAccountStatus(id);
        return "redirect:/bankAccount?pageNumber=1";
    }
}
