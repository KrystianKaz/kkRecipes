package com.example.kkRecipes.controller;

import com.example.kkRecipes.model.DailyDiet;
import com.example.kkRecipes.model.Meal;
import com.example.kkRecipes.model.User;
import com.example.kkRecipes.model.dto.analyzed_instructions.PreparationInstructionsDTO;
import com.example.kkRecipes.model.dto.complex_search.ComplexSearchDTO;
import com.example.kkRecipes.model.dto.complex_search.ComplexSearchValuesDTO;
import com.example.kkRecipes.model.dto.meal_plan.MealPlanDTO;
import com.example.kkRecipes.model.dto.meal_plan.MealPlanSearchValuesDTO;
import com.example.kkRecipes.model.dto.meal_plan.MealPlanWeekDTO;
import com.example.kkRecipes.model.dto.nutrients_search.NutrientsSearchResultsDTO;
import com.example.kkRecipes.model.dto.nutrients_search.NutrientsSearchValuesDTO;
import com.example.kkRecipes.model.dto.recipe.RecipeDTO;
import com.example.kkRecipes.service.DailyDietService;
import com.example.kkRecipes.service.MealService;
import com.example.kkRecipes.service.RecipeService;
import com.example.kkRecipes.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;
    private final MealService mealService;
    private final UserService userService;
    private final DailyDietService dailyDietService;

    @GetMapping("/recipes/{id}")
    public String getRecipeById(@PathVariable int id, Model model, Principal principal) {
        RecipeDTO mealById = recipeService.recipeById(id);
        model.addAttribute("mealById", mealById);

        List<PreparationInstructionsDTO> preparationInstructionsDTOS = recipeService.preparationInstructionsByRecipeId(id);
        model.addAttribute("preps", preparationInstructionsDTOS);

        model.addAttribute("converter", recipeService);

        String nutritionLabel = recipeService.recipeNutritionLabel(id);
        model.addAttribute("label", nutritionLabel);

        if (principal != null) {
            User user = userService.findUserByUsername(principal.getName());

            boolean wasMealAddedToLikedByUser = mealService.wasMealAddedToLikedByUser(id, user);
            model.addAttribute("wasMealAddedToLikedByUser", wasMealAddedToLikedByUser);

            boolean isMealStillLikedByUser = mealService.isMealStillLikedByUser(id, user);
            model.addAttribute("stillLiked", isMealStillLikedByUser);
        }

        return "result_pages/meal";
    }

    @GetMapping("/search")
    public String getComplexSearchPage() {
        return "search_pages/search";
    }

    @GetMapping("/complexSearch")
    public String getComplexSearchResults(ComplexSearchValuesDTO complexSearchValuesDTO, Model model) {
        ComplexSearchDTO complexSearchDTO = recipeService.recipeComplexSearch(complexSearchValuesDTO);
        model.addAttribute("meals", complexSearchDTO);

        return "result_pages/meal-list";
    }

    @GetMapping("/findByNutrients")
    public String getNutrientsSearchPage() {
        return "search_pages/searchByNutrients";
    }

    @GetMapping("/foundByNutrients")
    public String resultsByNutrientsSearch(NutrientsSearchValuesDTO nutrientsSearchValuesDTO, Model model) {
        List<NutrientsSearchResultsDTO> nutrientsSearchDTO = recipeService.recipeNutrientsSearch(nutrientsSearchValuesDTO);
        model.addAttribute("nutrients", nutrientsSearchDTO);

        return "result_pages/mealsByNutrients-list";
    }

    @GetMapping("/generateDailyMealPlan")
    public String getDailyMealPlanGeneratorPage() {
        return "search_pages/daily-generator";
    }

    @GetMapping("/dailyMealPlan")
    public String generatedDailyMealPlanPage(MealPlanSearchValuesDTO mealPlanSearchValuesDTO,
                                             HttpSession session,
                                             Model model) {
        MealPlanDTO mealPlanDTO = recipeService.generateDailyMealPlan(mealPlanSearchValuesDTO);
        session.setAttribute("plan", mealPlanDTO);
        model.addAttribute("plan", mealPlanDTO);

        return "result_pages/daily-list";
    }

    @GetMapping("/saveDailyMealPlan")
    public String getDailyPlanSavingPage() {
        return "daily_plan/daily-saving";
    }


    @PostMapping("/saveDailyMealPlan")
    public RedirectView saveDailyMealPlanToUser(DailyDiet diet, HttpSession session, Principal principal) {

        MealPlanDTO mealPlanDTO = (MealPlanDTO) session.getAttribute("plan");
        User user = userService.findUserByUsername(principal.getName());

        dailyDietService.saveDailyDiet(diet, mealPlanDTO, user);
        session.removeAttribute("plan");

        return new RedirectView("/user");
    }

    @GetMapping("/generateWeeklyMealPlan")
    public String getWeeklyMealPlanGeneratorPage() {
        return "search_pages/weekly-generator";
    }

    @GetMapping("/weeklyMealPlan")
    public String generatedWeeklyMealPlanPage(MealPlanSearchValuesDTO mealPlanSearchValuesDTO, Model model) {
        MealPlanWeekDTO mealPlanWeekDTO = recipeService.generateWeeklyMealPlan(mealPlanSearchValuesDTO);
        model.addAttribute("dto", mealPlanWeekDTO);

        return "result_pages/weekly-list";
    }

    @PostMapping("/recipes/{id}/add")
    public RedirectView addMealToLikedByUser(@PathVariable("id") int id, Principal principal, Meal meal) {
        mealService.addMealToLikedByUser(id, userService.findUserByUsername(principal.getName()), meal);
        return new RedirectView("/recipes/{id}");
    }

    @PostMapping("/recipes/{id}/liked")
    public RedirectView likedMealStatusChanger(@PathVariable("id") int id, Principal principal, Meal meal) {
        mealService.addOrRemoveMealFromLikedByUser(id, userService.findUserByUsername(principal.getName()));
        return new RedirectView("/recipes/{id}");
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public String handleClientError(HttpClientErrorException.NotFound e, Model model) {
        model.addAttribute("statusCode", e.getStatusCode());
        model.addAttribute("message", extractErrorMessage(e.getMessage()));
        return "/errors/client";
    }

    private String extractErrorMessage(String errorMessage) {
        String prefix = "message\":\"";
        String suffix = "\"}";
        int startIndex = errorMessage.indexOf(prefix);
        int endIndex = errorMessage.indexOf(suffix, startIndex + prefix.length());
        if (startIndex != -1 && endIndex != -1) {
            return errorMessage.substring(startIndex + prefix.length(), endIndex);
        } else {
            return errorMessage;
        }
    }
}