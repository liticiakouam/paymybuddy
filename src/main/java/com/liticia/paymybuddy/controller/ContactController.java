package com.liticia.paymybuddy.controller;

import com.liticia.paymybuddy.Entity.Contact;
import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Service.ContactService;
import com.liticia.paymybuddy.exception.ContactNotFoundException;
import com.liticia.paymybuddy.exception.NotSupportedActionException;
import com.liticia.paymybuddy.exception.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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

            return "redirect:/user?pageNumber=1";
        } catch (ContactNotFoundException e) {
            redirectAttributes.addFlashAttribute("userNotFound", "User not found");
            model.addAttribute("user", new User());

            return "redirect:/user?pageNumber=1";
        }    catch (NotSupportedActionException e) {
            redirectAttributes.addFlashAttribute("notSupported", "You cannot be added as a contact");
            model.addAttribute("user", new User());

            return "redirect:/user?pageNumber=1";
        }
        return "redirect:/user?pageNumber=1";
    }

    @GetMapping("/contact")
    public String getContacts(@RequestParam("pageNumber") int pageNumber, Model model) {
        return findAll(pageNumber, model);
    }

    private String findAll(@RequestParam("pageNumber") int pageNumber,
                           Model model
    ) {

        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        Page<Contact> page = contactService.findPaginated(pageable);
        List<Contact> contacts = page.getContent();

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("contacts", contacts);
        return "contact";
    }

    @GetMapping("/contact/remove/{id}")
    public String removeUserFromYourFriendList (@PathVariable(value = "id") long id) {
        contactService.removeUser(id);
        return "redirect:/contact?pageNumber=1";
    }

}
