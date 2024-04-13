package com.liticia.paymybuddy.controller;

import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Service.UserService;
import com.liticia.paymybuddy.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class HomePageController {
    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String homePage(Model model) {
        Optional<User> optionalUser = userService.findById(SecurityUtils.getCurrentUserId());
        model.addAttribute("user", optionalUser.get());
        return "home";
    }
}
