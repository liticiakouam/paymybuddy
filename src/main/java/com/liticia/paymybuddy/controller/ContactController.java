package com.liticia.paymybuddy.controller;

import com.liticia.paymybuddy.Entity.*;
import com.liticia.paymybuddy.Service.BankAccountService;
import com.liticia.paymybuddy.Service.ContactService;
import com.liticia.paymybuddy.Service.OperationService;
import com.liticia.paymybuddy.Service.UserService;
import com.liticia.paymybuddy.dto.ContactCreated;
import com.liticia.paymybuddy.dto.OperationCreate;
import com.liticia.paymybuddy.exception.*;
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
    public String addUser(@PathVariable(value = "friendId") long friendId, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("user", new User());
            contactService.save(friendId);
            redirectAttributes.addFlashAttribute("userAdd", "This user has been successfully add to your friend");

        } catch (UserAlreadyExistException e) {
            redirectAttributes.addFlashAttribute("userExist", "This user is already your friend");
            model.addAttribute("user", new User());

            return "redirect:/users";
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("userNotFound", "User not found");
            model.addAttribute("user", new User());

            return "redirect:/users";
        }
        return "redirect:/users";
    }

}
