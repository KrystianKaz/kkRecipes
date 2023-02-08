package com.example.kkRecipes.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UriUtils {

    public static final String API_BASIC_URL = "https://api.spoonacular.com/";
    public static final String API_RECIPE_URL = API_BASIC_URL + "recipes/";
    public static final String API_COMPLEX_URL = API_RECIPE_URL + "complexSearch";
    public static final String API_FIND_BY_NUTRIENTS_URL = API_RECIPE_URL + "findByNutrients";

    public static String getRecipeById(int id) {
        return API_RECIPE_URL + id + "/information";
    }

    public static String getRecipePreparationInstructions(int id) {
        return API_RECIPE_URL + id + "/analyzedInstructions";
    }

}
