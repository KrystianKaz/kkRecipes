package com.example.kkRecipes.model.dto.meal_plan;

import lombok.Data;

@Data
public class MealPlanDaysDTO {
    private MealPlanDTO monday;
    private MealPlanDTO tuesday;
    private MealPlanDTO wednesday;
    private MealPlanDTO thursday;
    private MealPlanDTO friday;
    private MealPlanDTO saturday;
    private MealPlanDTO sunday;
}
