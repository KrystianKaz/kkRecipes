package com.example.kkRecipes.model.dto.meal_plan;

import lombok.Data;

import java.util.List;

@Data
public class MealPlanDTO {
    private List<MealPlanValuesDTO> meals;
}
