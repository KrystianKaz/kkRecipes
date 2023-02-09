package com.example.kkRecipes.service;

import com.example.kkRecipes.model.dto.analyzed_instructions.InstructionStepsDTO;
import com.example.kkRecipes.model.dto.analyzed_instructions.PreparationInstructionsDTO;
import com.example.kkRecipes.model.dto.complex_search.ComplexSearchDTO;
import com.example.kkRecipes.model.dto.complex_search.ComplexSearchValuesDTO;
import com.example.kkRecipes.model.dto.meal_plan.MealPlanDTO;
import com.example.kkRecipes.model.dto.meal_plan.MealPlanSearchValuesDTO;
import com.example.kkRecipes.model.dto.nutrients_search.NutrientsSearchResultsDTO;
import com.example.kkRecipes.model.dto.nutrients_search.NutrientsSearchValuesDTO;
import com.example.kkRecipes.model.dto.recipe.RecipeDTO;
import com.example.kkRecipes.webclient.RecipeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
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

    //todo- implementing recipe
//    public List<InstructionStepsDTO> getInstructions(List<PreparationInstructionsDTO> dtos, Integer id) {
//        List<InstructionStepsDTO> steps = dtos.get(0).getSteps();
//        return steps;
//    }

    public ComplexSearchDTO recipeComplexSearch(ComplexSearchValuesDTO complexSearchValuesDTO) {
        return recipeClient.complexSearch(complexSearchValuesDTO);
    }

    public List<NutrientsSearchResultsDTO> recipeNutrientsSearch(NutrientsSearchValuesDTO nutrientsSearchValuesDTO) {
        return recipeClient.searchByNutrients(nutrientsSearchValuesDTO);
    }

    public MealPlanDTO generateDailyMealPlan(MealPlanSearchValuesDTO mealPlanSearchValuesDTO) {
        MealPlanDTO mealPlanDTO = recipeClient.generateDailyMealPlan(mealPlanSearchValuesDTO);
        setImagesForMealPlans(mealPlanDTO);
        return mealPlanDTO;
    }

    private void setImagesForMealPlans(MealPlanDTO mealPlanDTO) {
        mealPlanDTO.getMeals()
                .forEach(meal -> meal.setImage(recipeById(meal.getId()).getImage()));
    }
}
