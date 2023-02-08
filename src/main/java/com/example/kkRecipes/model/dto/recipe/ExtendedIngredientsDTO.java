package com.example.kkRecipes.model.dto.recipe;

import lombok.Data;
@Data
public class ExtendedIngredientsDTO {
    private String aisle;
    private String image;
    private String name;
    private String original;
    private double amount;
    private String unit;
    private RecipeMeasuresDTO measures;
}
