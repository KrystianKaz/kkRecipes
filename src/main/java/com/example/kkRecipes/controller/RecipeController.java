package com.example.kkRecipes.controller;

import com.example.kkRecipes.model.dto.recipe.RecipeDTO;
import com.example.kkRecipes.model.dto.analyzed_instructions.PreparationInstructionsDTO;
import com.example.kkRecipes.model.dto.complex_search.ComplexSearchDTO;
import com.example.kkRecipes.model.dto.complex_search.MealDetailsDTO;
import com.example.kkRecipes.model.dto.nutrients_search.NutrientsSearchResultsDTO;
import com.example.kkRecipes.model.dto.nutrients_search.NutrientsSearchValuesDTO;
import com.example.kkRecipes.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/")
    public String getHomePage() {
        return "index";
    }

    @GetMapping("/recipes/{id}")
    public String getRecipeById(@PathVariable Integer id, Model model) throws IOException {
        RecipeDTO mealById = recipeService.recipeById(id);
        model.addAttribute("mealById", mealById);
        List<PreparationInstructionsDTO> preparationInstructionsDTOS = recipeService.preparationInstructionsByRecipeId(id);
        model.addAttribute("preps", preparationInstructionsDTOS);
        return "meal";
    }
    @GetMapping("/search")
    public String getComplexSearchPage() {
        return "search";
    }

    @GetMapping("/complexSearch")
    public String getComplexSearchResults(MealDetailsDTO mealDetailsDTO, Model model) {
        ComplexSearchDTO complexSearchDTO = recipeService.recipeComplexSearch(mealDetailsDTO);
        int totalResults = complexSearchDTO.getTotalResults();
        model.addAttribute("meals", complexSearchDTO);
        model.addAttribute("size", totalResults);
        return "meal-list";
    }

    @GetMapping("/findByNutrients")
    public String getNutrientsSearchPage() {
        return "searchByNutrients";
    }

    @GetMapping("/foundByNutrients")
    public String resultsByNutrientsSearch(NutrientsSearchValuesDTO nutrientsSearchValuesDTO, Model model){
        List<NutrientsSearchResultsDTO> nutrientsSearchDTO = recipeService.recipeNutrientsSearch(nutrientsSearchValuesDTO);
        model.addAttribute("nutrients", nutrientsSearchDTO);
        return "mealsByNutrients-list";
    }
}
