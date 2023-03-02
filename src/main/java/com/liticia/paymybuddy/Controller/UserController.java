package com.liticia.paymybuddy.Controller;

import com.liticia.paymybuddy.Entity.User;
import com.liticia.paymybuddy.Service.UserService;
import com.liticia.paymybuddy.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

/*    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String registerGet (Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String postUserAdd(@ModelAttribute("user") UserDTO userDTO) {

        User user = new User();
        user.setId(userDTO.getId());
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        userService.save(user);
        return "redirect:/home";
    }*/
}
