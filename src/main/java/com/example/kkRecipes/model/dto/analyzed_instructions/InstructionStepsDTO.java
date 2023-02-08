package com.example.kkRecipes.model.dto.analyzed_instructions;

import lombok.Data;

import java.util.List;

@Data
public class InstructionStepsDTO {
    public int number;
    private String step;
    private List<IngredientsDTO> ingredients;
    private List<EquipmentDTO> equipment;
}
