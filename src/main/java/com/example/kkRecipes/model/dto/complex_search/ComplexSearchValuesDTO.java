package com.example.kkRecipes.model.dto.complex_search;

import lombok.Data;

@Data
public class ComplexSearchValuesDTO {

    private String query;
    private String cuisine;
    private String diet;
    private String intolerances;
    private String type;

}
