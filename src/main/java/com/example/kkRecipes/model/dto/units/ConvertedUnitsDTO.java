package com.example.kkRecipes.model.dto.units;

import lombok.Data;

import java.text.DecimalFormat;

@Data
public class ConvertedUnitsDTO {

    private double targetAmount;
    private String targetUnit;
    private String convertedAmount;

    public String getConvertedAmount() {
        DecimalFormat df = new DecimalFormat("0.#");
        return df.format(targetAmount);
    }
}
