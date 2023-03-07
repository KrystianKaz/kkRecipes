package com.example.kkRecipes.model.dto.nutrients_search;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NutrientsSearchResultsDTO {
    private String title;
    private String image;
    private int id;
    private int calories;
    private String fat;
    private String carbs;
    private String protein;

    public NutrientsSearchResultsDTO(final String title, final String protein) {
        this.title = title;
        this.protein = protein;
    }
}
