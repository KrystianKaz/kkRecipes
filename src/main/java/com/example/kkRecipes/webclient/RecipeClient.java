package com.example.kkRecipes.webclient;

import com.example.kkRecipes.model.dto.recipe.RecipeDTO;
import com.example.kkRecipes.model.dto.analyzed_instructions.PreparationInstructionsDTO;
import com.example.kkRecipes.model.dto.complex_search.ComplexSearchDTO;
import com.example.kkRecipes.model.dto.complex_search.MealDetailsDTO;
import com.example.kkRecipes.model.dto.nutrients_search.NutrientsSearchResultsDTO;
import com.example.kkRecipes.model.dto.nutrients_search.NutrientsSearchValuesDTO;
import com.example.kkRecipes.utils.UriUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static com.example.kkRecipes.utils.UriUtils.API_COMPLEX_URL;
import static com.example.kkRecipes.utils.UriUtils.API_FIND_BY_NUTRIENTS_URL;

@Component
@Slf4j
public class RecipeClient {


    private final RestTemplate restTemplate = new RestTemplate();

    private final String API_KEY = "810e411c428940feb0d2463a9f3de9d3";

    public RecipeDTO recipeById(Integer id) {
        return restTemplate.getForObject(UriUtils.getRecipeById(id) + getApiKey(), RecipeDTO.class);
    }

    public ComplexSearchDTO complexSearch(MealDetailsDTO mealDetailsDTO) {
        return restTemplate.getForObject(API_COMPLEX_URL + getApiKey() +
                        "&query={query}&cuisine={cuisine}&diet={diet}&intolerances={intolerances}&type={type}",
                ComplexSearchDTO.class,
                mealDetailsDTO.getQuery(),
                mealDetailsDTO.getCuisine(),
                mealDetailsDTO.getDiet(),
                mealDetailsDTO.getIntolerances(),
                mealDetailsDTO.getType());
    }

    public List<NutrientsSearchResultsDTO> searchByNutrients(NutrientsSearchValuesDTO nutrientsSearchValuesDTO) {
        NutrientsSearchResultsDTO[] forArray = restTemplate.getForObject(API_FIND_BY_NUTRIENTS_URL + getApiKey() + "&minCalories={minCalories}" +
                        "&maxCalories={maxCalories}" +
                        "&minFat={minFat}" +
                        "&maxFat={maxFat}" +
                        "&minCarbs={minCarbs}" +
                        "&maxCarbs={maxCarbs}" +
                        "&minProtein={minProtein}" +
                        "&maxProtein={maxProtein}",
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

    public List<PreparationInstructionsDTO> preparationInstructions(Integer id) {
        PreparationInstructionsDTO[] forArray = restTemplate
                .getForObject(UriUtils.getRecipePreparationInstructions(id) + getApiKey(),
                        PreparationInstructionsDTO[].class);
        assert forArray != null;
        return Arrays.asList(forArray);
    }

    private String getApiKey() {
        return "?apiKey=" + API_KEY;
    }
}
