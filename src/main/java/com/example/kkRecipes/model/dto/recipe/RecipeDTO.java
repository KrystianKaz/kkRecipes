package com.example.kkRecipes.model.dto.recipe;

import lombok.Data;

import java.util.List;

@Data
public class RecipeDTO {

    private String title;
    private String sourceUrl;
    private String image;
    private int readyInMinutes;
    private int servings;
    private String summary;
    private List<ExtendedIngredientsDTO> extendedIngredients;
}
