package com.example.kkRecipes.model.dto.complex_search;

import lombok.Data;

import java.util.List;

@Data
public class ComplexSearchDTO {
    private List<ComplexSearchResultsDTO> results;
    private int totalResults;
}
