package com.example.kkRecipes.model.dto.nutrients_search;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class NutrientsSearchValuesDTO {
    @Min(value = 0, message = "Target calories value should be higher!")
    private Integer minCalories;
    private Integer maxCalories;
    @Min(value = 0, message = "Target fat value should be higher!")
    private Integer minFat;
    private Integer maxFat;
    @Min(value = 0, message = "Target carbohydrates value should be higher!")
    private Integer minCarbs;
    private Integer maxCarbs;
    @Min(value = 0, message = "Target protein value should be higher!")
    private Integer minProtein;
    private Integer maxProtein;
}
