package com.liticia.paymybuddy.controller;

import com.liticia.paymybuddy.Entity.*;
import com.liticia.paymybuddy.Service.BankAccountService;
import com.liticia.paymybuddy.Service.ContactService;
import com.liticia.paymybuddy.Service.OperationService;
import com.liticia.paymybuddy.dto.ContactCreated;
import com.liticia.paymybuddy.dto.OperationCreate;
import com.liticia.paymybuddy.exception.BankAccountNotFoundException;
import com.liticia.paymybuddy.exception.InsufficientBalanceException;
import com.liticia.paymybuddy.exception.NotSupportedOperationException;
import com.liticia.paymybuddy.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ContactController {
    @Autowired
    private ContactService contactService;

    @GetMapping("/addUser/{friendId}")
    public String addUser(@PathVariable(value = "friendId") long friendId, @ModelAttribute("contactCreated") ContactCreated contactCreated) {
        contactCreated.setFriendId(friendId);

        contactService.save(contactCreated);
        return "redirect:users";
    }

}
