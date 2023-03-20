package com.liticia.paymybuddy.controller;

import org.springframework.stereotype.Controller;

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
