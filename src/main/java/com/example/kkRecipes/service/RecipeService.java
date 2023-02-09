package com.example.kkRecipes.service;

import com.example.kkRecipes.model.dto.analyzed_instructions.PreparationInstructionsDTO;
import com.example.kkRecipes.model.dto.complex_search.ComplexSearchDTO;
import com.example.kkRecipes.model.dto.complex_search.ComplexSearchValuesDTO;
import com.example.kkRecipes.model.dto.meal_plan.MealPlanDTO;
import com.example.kkRecipes.model.dto.meal_plan.MealPlanSearchValuesDTO;
import com.example.kkRecipes.model.dto.meal_plan.MealPlanWeekDTO;
import com.example.kkRecipes.model.dto.nutrients_search.NutrientsSearchResultsDTO;
import com.example.kkRecipes.model.dto.nutrients_search.NutrientsSearchValuesDTO;
import com.example.kkRecipes.model.dto.recipe.RecipeDTO;
import com.example.kkRecipes.webclient.RecipeClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {

    private final RecipeClient recipeClient;

    public RecipeService(final RecipeClient recipeClient) {
        this.recipeClient = recipeClient;
    }

    public RecipeDTO recipeById(Integer id) {
        return recipeClient.recipeById(id);
    }

    public List<PreparationInstructionsDTO> preparationInstructionsByRecipeId(Integer id) {
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
        MealPlanWeekDTO mealPlanWeekDTO = recipeClient.generateWeeklyMealPlan(mealPlanSearchValuesDTO);
        setImagesForWeeklyMealPlans(mealPlanWeekDTO);
        return mealPlanWeekDTO;
    }

    private void setImagesForDailyMealPlans(MealPlanDTO mealPlanDTO) {
        mealPlanDTO.getMeals()
                .forEach(meal -> meal.setImage(recipeById(meal.getId()).getImage()));
    }

    private void setImagesForWeeklyMealPlans(MealPlanWeekDTO mealPlanWeekDTO) {
        mealPlanWeekDTO
                .getWeek()
                .getMonday()
                .getMeals()
                .forEach(meal -> meal.setImage(recipeById(meal.getId()).getImage()));
        mealPlanWeekDTO.getWeek().getFriday().getMeals().forEach(meal -> meal.setImage(recipeById(meal.getId()).getImage()));
    }
}
