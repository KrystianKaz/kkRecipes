package com.example.kkRecipes.service;

import com.example.kkRecipes.model.dto.analyzed_instructions.PreparationInstructionsDTO;
import com.example.kkRecipes.model.dto.complex_search.ComplexSearchDTO;
import com.example.kkRecipes.model.dto.complex_search.ComplexSearchValuesDTO;
import com.example.kkRecipes.model.dto.meal_plan.MealPlanDTO;
import com.example.kkRecipes.model.dto.meal_plan.MealPlanSearchValuesDTO;
import com.example.kkRecipes.model.dto.meal_plan.MealPlanWeekDTO;
import com.example.kkRecipes.model.dto.nutrients_search.NutrientsSearchResultsDTO;
import com.example.kkRecipes.model.dto.nutrients_search.NutrientsSearchValuesDTO;
import com.example.kkRecipes.model.dto.recipe.AmountUnitDTO;
import com.example.kkRecipes.model.dto.recipe.ExtendedIngredientsDTO;
import com.example.kkRecipes.model.dto.recipe.RecipeDTO;
import com.example.kkRecipes.model.dto.units.ConvertedUnitsDTO;
import com.example.kkRecipes.webclient.RecipeClient;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class RecipeService {

    private final RecipeClient recipeClient;

    public RecipeDTO recipeById(int id) {
        return recipeClient.recipeById(id);
    }

    public List<PreparationInstructionsDTO> preparationInstructionsByRecipeId(int id) {
        return recipeClient.preparationInstructions(id);
    }

    public ComplexSearchDTO recipeComplexSearch(ComplexSearchValuesDTO complexSearchValuesDTO) {
        return recipeClient.complexSearch(complexSearchValuesDTO);
    }

    public List<NutrientsSearchResultsDTO> recipeNutrientsSearch(NutrientsSearchValuesDTO nutrientsSearchValuesDTO) {
        return recipeClient.searchByNutrients(nutrientsSearchValuesDTO);
    }

    public MealPlanDTO generateDailyMealPlan(MealPlanSearchValuesDTO mealPlanSearchValuesDTO) {
        MealPlanDTO mealPlanDTO = recipeClient.generateDailyMealPlan(mealPlanSearchValuesDTO);
        setImagesForDailyMealPlans(mealPlanDTO);
        return mealPlanDTO;
    }

    public MealPlanWeekDTO generateWeeklyMealPlan(MealPlanSearchValuesDTO mealPlanSearchValuesDTO) {
        return recipeClient.generateWeeklyMealPlan(mealPlanSearchValuesDTO);
    }

    private void setImagesForDailyMealPlans(MealPlanDTO mealPlanDTO) {
        mealPlanDTO.getMeals()
                .forEach(meal -> meal.setImage(recipeById(meal.getId()).getImage()));
    }

    public ConvertedUnitsDTO convertUnitsToGrams(String amount, String unit, String name) {
        String replacedAmount = amount.replace(",", ".");
        double amountSeparatedWithDot = Double.parseDouble(replacedAmount);

        return  recipeClient.convertedUnitsForRecipe(amountSeparatedWithDot, unit, name);
    }
}
