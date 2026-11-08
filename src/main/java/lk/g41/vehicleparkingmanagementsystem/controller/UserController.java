package lk.g41.vehicleparkingmanagementsystem.controller;

import lk.g41.vehicleparkingmanagementsystem.model.User;
import lk.g41.vehicleparkingmanagementsystem.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/users/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        return "add-user";
    }

    @PostMapping("/users/add")
    public String addUser(User user, Model model) {
        if (userService.addUser(user)) {
            return "redirect:/users";
        }

        model.addAttribute("error",
                "User ID or username already exists.");
        model.addAttribute("user", user);
        return "add-user";
    }

    @GetMapping("/users/edit")
    public String showEditForm(
            @RequestParam String userId, Model model) {

        User user = userService.findUser(userId);

        if (user == null) {
            return "redirect:/users";
        }

        model.addAttribute("user", user);
        return "edit-user";
    }

    @PostMapping("/users/edit")
    public String updateUser(User user, Model model) {
        if (userService.updateUser(user)) {
            return "redirect:/users";
        }

        model.addAttribute("error",
                "User not found or username is already in use.");
        model.addAttribute("user", user);
        return "edit-user";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam String userId) {
        userService.deleteUser(userId);
        return "redirect:/users";
    }
}