package com.example.kkRecipes.webclient;

import com.example.kkRecipes.model.dto.analyzed_instructions.PreparationInstructionsDTO;
import com.example.kkRecipes.model.dto.complex_search.ComplexSearchDTO;
import com.example.kkRecipes.model.dto.complex_search.ComplexSearchValuesDTO;
import com.example.kkRecipes.model.dto.meal_plan.MealPlanDTO;
import com.example.kkRecipes.model.dto.meal_plan.MealPlanSearchValuesDTO;
import com.example.kkRecipes.model.dto.meal_plan.MealPlanWeekDTO;
import com.example.kkRecipes.model.dto.nutrients_search.NutrientsSearchResultsDTO;
import com.example.kkRecipes.model.dto.nutrients_search.NutrientsSearchValuesDTO;
import com.example.kkRecipes.model.dto.recipe.RecipeDTO;
import com.example.kkRecipes.model.dto.units.ConvertedUnitsDTO;
import com.example.kkRecipes.utils.UriUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static com.example.kkRecipes.utils.UriUtils.*;

@Component
public class RecipeClient {


    private final RestTemplate restTemplate = new RestTemplate();

    private final String API_KEY;

    public RecipeClient(@Value("${kkrecipes.apiKeyValue}") String apiKey) {
        API_KEY = apiKey;
    }

    public RecipeDTO recipeById(int id) {
        return restTemplate.getForObject(UriUtils.getRecipeById(id) + getApiKey(), RecipeDTO.class);
    }

    public ComplexSearchDTO complexSearch(ComplexSearchValuesDTO complexSearchValuesDTO) {
        return restTemplate.getForObject(API_COMPLEX_URL + getApiKey() +
                        "&query={query}&cuisine={cuisine}&diet={diet}&intolerances={intolerances}&type={type}&number=12",
                ComplexSearchDTO.class,
                complexSearchValuesDTO.getQuery(),
                complexSearchValuesDTO.getCuisine(),
                complexSearchValuesDTO.getDiet(),
                complexSearchValuesDTO.getIntolerances(),
                complexSearchValuesDTO.getType());
    }

    public List<NutrientsSearchResultsDTO> searchByNutrients(NutrientsSearchValuesDTO nutrientsSearchValuesDTO) {
        NutrientsSearchResultsDTO[] forArray = restTemplate.getForObject(API_FIND_BY_NUTRIENTS_URL + getApiKey() +
                        "&minCalories={minCalories}" +
                        "&maxCalories={maxCalories}" +
                        "&minFat={minFat}" +
                        "&maxFat={maxFat}" +
                        "&minCarbs={minCarbs}" +
                        "&maxCarbs={maxCarbs}" +
                        "&minProtein={minProtein}" +
                        "&maxProtein={maxProtein}" +
                        "&number=12",
                NutrientsSearchResultsDTO[].class,
                nutrientsSearchValuesDTO.getMinCalories(),
                nutrientsSearchValuesDTO.getMaxCalories(),
                nutrientsSearchValuesDTO.getMinFat(),
                nutrientsSearchValuesDTO.getMaxFat(),
                nutrientsSearchValuesDTO.getMinCarbs(),
                nutrientsSearchValuesDTO.getMaxCarbs(),
                nutrientsSearchValuesDTO.getMinProtein(),
                nutrientsSearchValuesDTO.getMaxProtein());
        assert forArray != null;
        return Arrays.asList(forArray);
    }

    public List<PreparationInstructionsDTO> preparationInstructions(int id) {
        PreparationInstructionsDTO[] forArray = restTemplate
                .getForObject(UriUtils.getRecipePreparationInstructions(id) + getApiKey(),
                        PreparationInstructionsDTO[].class);
        assert forArray != null;
        return Arrays.asList(forArray);
    }

    public MealPlanDTO generateDailyMealPlan(MealPlanSearchValuesDTO mealPlanSearchValuesDTO) {
        return restTemplate.getForObject(API_MEAL_PLANNER + getApiKey() +
                        "&timeFrame=day&targetCalories={targetCalories}&diet={diet}&exclude={exclude}",
                MealPlanDTO.class,
                mealPlanSearchValuesDTO.getTargetCalories(),
                mealPlanSearchValuesDTO.getDiet(),
                mealPlanSearchValuesDTO.getExclude());
    }

    public MealPlanWeekDTO generateWeeklyMealPlan(MealPlanSearchValuesDTO mealPlanSearchValuesDTO) {
        return restTemplate.getForObject(API_MEAL_PLANNER + getApiKey() +
                        "&timeFrame=week&targetCalories={targetCalories}&diet={diet}&exclude={exclude}",
                MealPlanWeekDTO.class,
                mealPlanSearchValuesDTO.getTargetCalories(),
                mealPlanSearchValuesDTO.getDiet(),
                mealPlanSearchValuesDTO.getExclude());
    }

    public ConvertedUnitsDTO convertedUnitsForRecipe(double amount, String unitShort, String nameClean) {
        return restTemplate.getForObject(UriUtils.convertAllUnitsToGrams() + getApiKey() +
                "&sourceAmount={amount}&sourceUnit={unitShort}&ingredientName={nameClean}&targetUnit=grams",
                ConvertedUnitsDTO.class,
                amount,
                unitShort,
                nameClean);
    }

    private String getApiKey() {
        return "?apiKey=" + API_KEY;
    }
}
