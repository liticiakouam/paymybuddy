package com.liticia.paymybuddy.controller;

import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Entity.Operation;
import com.liticia.paymybuddy.Service.BankAccountService;
import com.liticia.paymybuddy.Service.OperationService;
import com.liticia.paymybuddy.dto.OperationCreate;
import com.liticia.paymybuddy.exception.OperationFailed;
import com.liticia.paymybuddy.exception.UserNotExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class OperationController {
    @Autowired
    private OperationService operationService;

    @Autowired
    private BankAccountService bankAccountService;

    @GetMapping("/operation")
    public String getOperations(@RequestParam("pageNumber") int pageNumber,
                                Model model) {
        model.addAttribute("operation", new OperationCreate());
        return findPaginated(pageNumber, model);
    }

    private String findPaginated(@RequestParam("pageNumber") int pageNumber,
                                 Model model) {

        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        Page<Operation> page = operationService.findPaginated(pageable);
        List<Operation> operations = page.getContent();
        List<BankAccount> bankAccounts = bankAccountService.findActiveAccountNumber(true);

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("operations", operations);
        model.addAttribute("bankAccounts", bankAccounts);
        return "operation";
    }

    @PostMapping("/operation/add")
    public String postOperation(@ModelAttribute("operation") OperationCreate operationCreate, Model model,
                                RedirectAttributes redirectAttributes) throws Exception {
        try {
            operationService.saveCreditedAccount(operationCreate);
            redirectAttributes.addFlashAttribute("saved", "Operation successfully save!");
        } catch (OperationFailed ex) {
            redirectAttributes.addFlashAttribute("operationFailed","Sorry an error has occurred, the operation cannot be completed");
            model.addAttribute("operations", operationService.getAll());

            return "redirect:/operation?pageNumber=1";
        }

        return "redirect:/operation?pageNumber=1";
    }

}
