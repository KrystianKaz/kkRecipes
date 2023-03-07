package com.example.kkRecipes.model.dto.meal_plan;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MealPlanValuesDTO {
    private int id;
    private String title;
    private int servings;
    private String image;

    public MealPlanValuesDTO(final int id, final String title) {
        this.id = id;
        this.title = title;
    }
}
