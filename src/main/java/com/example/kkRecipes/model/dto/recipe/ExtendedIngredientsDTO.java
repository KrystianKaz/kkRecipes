package com.example.kkRecipes.model.dto.recipe;

import lombok.Data;
@Data
public class ExtendedIngredientsDTO {
    private String image;
    private String name;
    private String original;
    private String originalName;
    private double amount;
    private String unit;
}
