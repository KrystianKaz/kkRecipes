package com.example.kkRecipes.model.dto.meal_plan;

import lombok.Data;

@Data
public class MealPlanSearchValuesDTO {
    private String timeFrame;
    private int targetCalories;
    private String diet;
    private String exclude;
}
