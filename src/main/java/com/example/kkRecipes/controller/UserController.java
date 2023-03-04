package com.example.kkRecipes.controller;

import com.example.kkRecipes.model.Meal;
import com.example.kkRecipes.service.MealService;
import com.example.kkRecipes.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final MealService mealService;
    private final UserService userService;

    @GetMapping("/user")
    public String showUsersPanel() {
        return "user_panel/user";
    }

    @GetMapping("/likedMeals")
    public String showUsersLikedRecipes(Principal principal, Model model) {
        List<Meal> meals = mealService.showUsersLikedMeals(userService.findUserByUsername(principal.getName()));
        model.addAttribute("meals", meals);
        return "user_panel/user_pages/liked-meals";
    }
}
