package com.example.kkRecipes.model.dto.meal_plan;

import lombok.Data;

@Data
public class MealPlanSearchValuesDTO {
    private int targetCalories;
    private String diet;
    private String exclude;
}
