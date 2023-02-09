package com.example.kkRecipes.model.dto.meal_plan;

import lombok.Data;

@Data
public class MealPlanValuesDTO {
    private int id;
    private String title;
    private int servings;
    private String image;
}
