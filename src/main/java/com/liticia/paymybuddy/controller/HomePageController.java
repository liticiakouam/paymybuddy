package com.liticia.paymybuddy.controller;

import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Service.BankAccountService;
import com.liticia.paymybuddy.Service.UserService;
import com.liticia.paymybuddy.dto.BankAccountCreate;
import com.liticia.paymybuddy.exception.BankAccountAlreadyExist;
import com.liticia.paymybuddy.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
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
