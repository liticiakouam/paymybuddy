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

import javax.validation.Valid;

@Controller
@AllArgsConstructor
public class LoginController {
    private UserService userService;

    @GetMapping({"/", " "})
    public String index(){
        return "landingPage";
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
                               BindingResult result,
                               Model model){
        try {
            userService.saveUser(userDto);
        } catch (UserAlreadyExistException e) {
            result.rejectValue("email", null,
                    "User already exist with the same email");
            model.addAttribute("user", userDto);
            return "/register";
        }

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }



}
