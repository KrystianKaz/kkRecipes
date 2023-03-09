package com.example.kkRecipes.controller;

import com.example.kkRecipes.exception.CreatedUserExistException;
import com.example.kkRecipes.exception.WrongEmailException;
import com.example.kkRecipes.exception.WrongPasswordException;
import com.example.kkRecipes.model.Meal;
import com.example.kkRecipes.model.User;
import com.example.kkRecipes.service.MealService;
import com.example.kkRecipes.service.UserService;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final MealService mealService;
    private final UserService userService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String getRegistrationPage() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(User user) {
        userService.registerUser(user.getEmail(), user.getUsername(), user.getPassword());

        return "auth/register-success";
    }

    @GetMapping("/user")
    public String showUserPanel() {
        return "user_panel/user";
    }

    @GetMapping("/likedMeals")
    public String showRecipesLikedByUser(Principal principal, Model model) {
        List<Meal> meals = mealService.showMealsCurrentlyLikedByUser(userService.findUserByUsername(principal.getName()));
        model.addAttribute("meals", meals);

        return "user_panel/user_pages/liked-meals";
    }

    @ExceptionHandler({CreatedUserExistException.class, WrongEmailException.class,
            WrongPasswordException.class, ConstraintViolationException.class})
    public String handleRegistrationErrors(RuntimeException e, Model model) {
        if(e.getMessage().contains("NotBlank")) {
            model.addAttribute("wrongData", "Given data cannot be blank!");
        } else model.addAttribute("wrongData", e.getMessage());
        return "auth/register";
    }

    @ExceptionHandler(SQLException.class)
    public String handleSQLErrors(SQLException e, Model model) {
        model.addAttribute("wrongData", e.getMessage());
        return "auth/register";
    }
}
