package com.liticia.paymybuddy.controller;

import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Service.ContactService;
import com.liticia.paymybuddy.exception.UserAlreadyExistException;
import com.liticia.paymybuddy.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

            return "redirect:/user?pageNumber=1";
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("userNotFound", "User not found");
            model.addAttribute("user", new User());

            return "redirect:/user?pageNumber=1";
        }
        return "redirect:/user?pageNumber=1";
    }

}
