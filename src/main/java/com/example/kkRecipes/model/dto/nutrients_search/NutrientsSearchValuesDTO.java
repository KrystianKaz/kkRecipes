package com.example.kkRecipes.model.dto.nutrients_search;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class NutrientsSearchValuesDTO {
    @Min(value = 0, message = "Target calories value should be higher!")
    private int minCalories;
    private int maxCalories;
    @Min(value = 0, message = "Target fat value should be higher!")
    private int minFat;
    private int maxFat;
    @Min(value = 0, message = "Target carbohydrates value should be higher!")
    private int minCarbs;
    private int maxCarbs;
    @Min(value = 0, message = "Target protein value should be higher!")
    private int minProtein;
    private int maxProtein;
}
