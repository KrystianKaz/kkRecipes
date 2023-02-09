package com.example.kkRecipes.model.dto.recipe;

import lombok.Data;

import java.text.DecimalFormat;

@Data
public class AmountUnitDTO {
    private double amount;
    private String unitShort;

    public String getAmount() {
        return new DecimalFormat("#.##").format(amount);
    }
}
