package com.example.kkRecipes.model.dto.nutrients_search;

import lombok.Data;

@Data
public class NutrientsSearchResultsDTO {
    private String title;
    private String image;
    private int id;
    private int calories;
    private String fat;
    private String carbs;
    private String protein;
}
