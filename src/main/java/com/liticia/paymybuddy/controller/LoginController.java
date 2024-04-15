package com.liticia.paymybuddy.controller;

import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Service.UserService;
import com.liticia.paymybuddy.dto.UserDto;
import com.liticia.paymybuddy.exception.UserAlreadyExistException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
public class LoginController {
    private UserService userService;

    @GetMapping({"/", " ", "/login"})
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    @GetMapping("/after-login")
    public String afterLogin() {
        return "redirect:/home";
    }

    @PostMapping("/register")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               RedirectAttributes attributes,
                               Model model){
        try {
            userService.saveUser(userDto);
        } catch (UserAlreadyExistException e) {
            attributes.addFlashAttribute("email",
                    "User already exist with the same email");
            model.addAttribute("user", userDto);
            return "redirect:/register";
        }

        return "redirect:/login";
    }

}
