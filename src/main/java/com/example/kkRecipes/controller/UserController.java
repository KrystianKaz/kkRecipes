package com.example.kkRecipes.controller;

import com.example.kkRecipes.exception.custom.CreatedUserExistException;
import com.example.kkRecipes.exception.custom.WrongEmailException;
import com.example.kkRecipes.exception.custom.WrongPasswordException;
import com.example.kkRecipes.model.DailyDiet;
import com.example.kkRecipes.model.Meal;
import com.example.kkRecipes.model.User;
import com.example.kkRecipes.service.DailyDietService;
import com.example.kkRecipes.service.MealService;
import com.example.kkRecipes.service.UserService;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final MealService mealService;
    private final UserService userService;
    private final DailyDietService dailyDietService;

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
        List<Meal> meals = mealService
                .showMealsCurrentlyLikedByUser(userService.findUserByUsername(principal.getName()));

        model.addAttribute("meals", meals);

        return "user_panel/user_pages/liked-meals";
    }

    @GetMapping("/savedDailyDiets")
    public String showDietsSavedByUser(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "3") int size,
                                       @RequestParam(defaultValue = "false") boolean isCompleted,
                                       @RequestParam(defaultValue = "false") boolean reverseOrder,
                                       Principal principal,
                                       Model model) {

        model.addAttribute("size", size);
        model.addAttribute("isCompleted", isCompleted);
        model.addAttribute("reverseOrder", reverseOrder);

        User userByUsername = userService.findUserByUsername(principal.getName());
        Pageable pageable = PageRequest.of(page, size);

        Page<DailyDiet> savedDietsByUser = dailyDietService
                .findSavedUserDietsAndPageOrSortThem(pageable, userByUsername, isCompleted, reverseOrder);
        model.addAttribute("savedDiets", savedDietsByUser);
        model.addAttribute("totalPages", savedDietsByUser.getTotalPages());
        model.addAttribute("pageNumber", page);


        return "user_panel/user_pages/saved-daily-diets";
    }

    @PostMapping("/changeCompletionStatus/{id}")
    public RedirectView changeCompletionStatusOfDailyPlan(@PathVariable long id, Principal principal) {
        User user = userService.findUserByUsername(principal.getName());

        dailyDietService.changeDailyMealPlanCompletionStatus(id, user);

        return new RedirectView("/savedDailyDiets");
    }

    @ExceptionHandler({CreatedUserExistException.class, WrongEmailException.class,
            WrongPasswordException.class, ConstraintViolationException.class})
    public String handleRegistrationErrors(RuntimeException e, Model model) {
        if (e.getMessage().contains("NotBlank")) {
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
