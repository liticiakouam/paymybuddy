package com.liticia.paymybuddy.controller;

import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Service.BankAccountService;
import com.liticia.paymybuddy.dto.BankAccountCreate;
import com.liticia.paymybuddy.exception.AccountNumberAlreadyExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @GetMapping("/bankAccount")
    public String getBankAccount(Model model) {
        BankAccountCreate bankAccount = new BankAccountCreate();
        model.addAttribute("bankAccount", bankAccount);
        model.addAttribute("bankAccounts", bankAccountService.getAll());
        return findPaginated(1, model);
    }


    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                Model model) {
        int pageSize = 5;

        Page <BankAccount> page = bankAccountService.findPaginated(pageNo, pageSize);
        List <BankAccount> bankAccounts = page.getContent();

        BankAccountCreate bankAccount = new BankAccountCreate();
        model.addAttribute("bankAccount", bankAccount);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("bankAccounts", bankAccounts);
        return "bankAccount";
    }
/*    @GetMapping("/bankAccount/add")
    public String showBankAccount (Model model) {
        BankAccount bankAccount = new BankAccount();
        model.addAttribute("bankAccount", bankAccount);
        return "bankAccount";
    }*/

    @PostMapping("/bankAccount/add")
    public String postBankAccount(
            @Valid @ModelAttribute("bankAccount") BankAccountCreate bankAccountCreate,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("bankAccounts", bankAccountService.getAll());
            return "bankAccount";
        }
        try {
           bankAccountService.save(bankAccountCreate);
           redirectAttributes.addFlashAttribute("saved", "The bankAccount is save!");
        } catch (AccountNumberAlreadyExist e) {
            model.addAttribute("message", "Sorry, the account number you are trying to insert already exist");
            model.addAttribute("bankAccounts", bankAccountService.getAll());
            return "bankAccount";
        }
        return "redirect:/bankAccount";
    }

/*    @PostMapping("/bankAccount/add")
    public String postBankAccount(@Valid @ModelAttribute("bankAccount") BankAccountCreate bankAccountCreate) {
        bankAccountService.save(bankAccountCreate);
        return "redirect:/bankAccount";
    }*/

    @GetMapping("/disable/{id}")
    public String updateBankAccount(@PathVariable int id) {
        bankAccountService.switchAccountStatus(id);
        return "redirect:/bankAccount";
    }
}
