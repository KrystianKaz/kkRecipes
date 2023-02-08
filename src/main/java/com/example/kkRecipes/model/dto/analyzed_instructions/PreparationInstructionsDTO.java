package com.example.kkRecipes.model.dto.analyzed_instructions;

import lombok.Data;

import java.util.List;

@Data
public class PreparationInstructionsDTO {
    private List<InstructionStepsDTO> steps;
}
