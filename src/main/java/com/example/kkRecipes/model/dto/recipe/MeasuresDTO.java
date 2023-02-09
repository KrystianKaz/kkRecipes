package com.example.kkRecipes.model.dto.recipe;

import lombok.Data;

@Data
public class MeasuresDTO {
    private AmountUnitDTO us;
    private AmountUnitDTO metric;
}
