package com.example.kkRecipes.controller;

import com.example.kkRecipes.model.dto.analyzed_instructions.PreparationInstructionsDTO;
import com.example.kkRecipes.model.dto.complex_search.ComplexSearchDTO;
import com.example.kkRecipes.model.dto.complex_search.ComplexSearchResultsDTO;
import com.example.kkRecipes.model.dto.complex_search.ComplexSearchValuesDTO;
import com.example.kkRecipes.model.dto.meal_plan.MealPlanDTO;
import com.example.kkRecipes.model.dto.meal_plan.MealPlanSearchValuesDTO;
import com.example.kkRecipes.model.dto.meal_plan.MealPlanWeekDTO;
import com.example.kkRecipes.model.dto.nutrients_search.NutrientsSearchResultsDTO;
import com.example.kkRecipes.model.dto.nutrients_search.NutrientsSearchValuesDTO;
import com.example.kkRecipes.model.dto.recipe.RecipeDTO;
import com.example.kkRecipes.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/")
    public String getHomePage() {
        return "index";
    }

    @GetMapping("/recipes/{id}")
    public String getRecipeById(@PathVariable Integer id, Model model){
        RecipeDTO mealById = recipeService.recipeById(id);
        model.addAttribute("mealById", mealById);

        List<PreparationInstructionsDTO> preparationInstructionsDTOS = recipeService.preparationInstructionsByRecipeId(id);
        model.addAttribute("preps", preparationInstructionsDTOS);

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
    public String resultsByNutrientsSearch(NutrientsSearchValuesDTO nutrientsSearchValuesDTO, Model model){
        List<NutrientsSearchResultsDTO> nutrientsSearchDTO = recipeService.recipeNutrientsSearch(nutrientsSearchValuesDTO);
        model.addAttribute("nutrients", nutrientsSearchDTO);

        return "result_pages/mealsByNutrients-list";
    }

    @GetMapping("/generateDailyMealPlan")
    public String getDailyMealPlanGeneratorPage() {
        return "search_pages/daily-generator";
    }

    @GetMapping("/dailyMealPlan")
    public String generatedDailyMealPlanPage(MealPlanSearchValuesDTO mealPlanSearchValuesDTO, Model model) {
        MealPlanDTO mealPlanDTO = recipeService.generateDailyMealPlan(mealPlanSearchValuesDTO);
        model.addAttribute("plan", mealPlanDTO);

        return "result_pages/daily-list";
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

}
