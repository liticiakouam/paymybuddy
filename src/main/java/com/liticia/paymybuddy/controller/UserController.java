package com.liticia.paymybuddy.controller;

import com.liticia.paymybuddy.Entity.BankAccount;
import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Service.UserService;
import com.liticia.paymybuddy.dto.BankAccountCreate;
import com.liticia.paymybuddy.dto.ContactCreated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user")//http://localhost:8080/users?pageNumber=1
    public String getUsers(
            @RequestParam("pageNumber") int pageNumber,
            Model model
    ) {
        model.addAttribute("contactCreated", new ContactCreated());
        return findPaginated(pageNumber, model);
    }

    @GetMapping("/user/search")
    public String getUserByFirstOrLastName (@Param("keyword") String keyword, Model model, RedirectAttributes redirectAttributes) {
        List<User> users = userService.search(keyword);
        int userSize = users.size();
        if (userSize > 0) {
            model.addAttribute("searchUsers", users);
            model.addAttribute("contactCreated", new ContactCreated());
            model.addAttribute("userSize", userSize);
        } else {
            redirectAttributes.addFlashAttribute("error", "sorry, there are no user existing with this word : " +keyword);
            return "user?pageNumber=1";
        }

        return "users";
    }

    private String findPaginated(@RequestParam("pageNumber") int pageNumber,
                                 Model model
    ) {

        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        Page<User> page = userService.findPaginated(pageable);
        List <User> users = page.getContent();

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("users", users);
        return "users";
    }
}
