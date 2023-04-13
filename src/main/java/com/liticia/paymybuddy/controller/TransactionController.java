package com.liticia.paymybuddy.controller;

import com.liticia.paymybuddy.Entity.Contact;
import com.liticia.paymybuddy.Entity.Transaction;
import com.liticia.paymybuddy.Service.ContactService;
import com.liticia.paymybuddy.Service.TransactionService;
import com.liticia.paymybuddy.dto.TransactionCreate;
import com.liticia.paymybuddy.exception.ContactNotFoundException;
import com.liticia.paymybuddy.exception.InsufficientBalanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ContactService contactService;

    @GetMapping("/transaction")
    public String getTransactions(@RequestParam("pageNumber") int pageNumber, Model model) {
        model.addAttribute("transaction", new TransactionCreate());
        model.addAttribute("contacts", contactService.getAll());

        return findAll(pageNumber, model);
    }

    @GetMapping("/transaction/contact")
    public String getTransactionById(@RequestParam("contactId") long contactId, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<Contact> optionalContact = contactService.findById(contactId);
            if (optionalContact.isEmpty()) {
                throw new ContactNotFoundException();
            }

            model.addAttribute("transaction", new TransactionCreate());
            model.addAttribute("contact", optionalContact.get());
            findAll(1, model);

        } catch (ContactNotFoundException e) {
            redirectAttributes.addFlashAttribute("contactNotFound", "Contact not found");
            return "redirect:/transaction?pageNumber=1";
        }

        return "transaction";
    }

    private String findAll(@RequestParam("pageNumber") int pageNumber, Model model) {
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        Page<Transaction> page = transactionService.findAll(pageable);
        List<Transaction> transactions = page.getContent();

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("transactions", transactions);

        return "transaction";
    }

    @PostMapping("/transaction/add")
    public String postTransaction(@ModelAttribute("transaction") TransactionCreate transactionCreate, RedirectAttributes redirectAttributes) {
        try {
            transactionService.save(transactionCreate);
            redirectAttributes.addFlashAttribute("transactionSave", "The transaction has been successfully achieved");

        } catch (ContactNotFoundException e) {
            redirectAttributes.addFlashAttribute("contactNotFound", "Contact not found");
            return "redirect:/transaction?pageNumber=1";

        } catch (InsufficientBalanceException ex) {
            redirectAttributes.addFlashAttribute("balanceError","Sorry, your balance is insufficient");
            return "redirect:/transaction?pageNumber=1";
        }

        return "redirect:/transaction?pageNumber=1";
    }
}
