package com.liticia.paymybuddy.controller;

import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String getUsers (Model model) {
        model.addAttribute("users", userService.getAll());
        model.addAttribute("user", new User());
        return "users";
    }

    @GetMapping("/users/search/{keyword}")
    public String getUserByFirstOrLastName (@PathVariable String keyword, Model model) {
        List<User> users = userService.searchByFirstnameOrLastname(keyword, keyword);
        int userSize = users.size();
        model.addAttribute("searchUsers", users);
        model.addAttribute("size", userSize);

        return "users";
    }

}
