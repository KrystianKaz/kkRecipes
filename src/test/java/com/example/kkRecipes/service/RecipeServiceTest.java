package com.example.kkRecipes.service;

import com.example.kkRecipes.model.dto.analyzed_instructions.InstructionStepsDTO;
import com.example.kkRecipes.model.dto.analyzed_instructions.PreparationInstructionsDTO;
import com.example.kkRecipes.model.dto.complex_search.ComplexSearchDTO;
import com.example.kkRecipes.model.dto.complex_search.ComplexSearchResultsDTO;
import com.example.kkRecipes.model.dto.complex_search.ComplexSearchValuesDTO;
import com.example.kkRecipes.model.dto.recipe.RecipeDTO;
import com.example.kkRecipes.webclient.RecipeClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @InjectMocks
    private RecipeService recipeService;
    @Mock
    private RecipeClient recipeClient;

    @Test
    void should_return_recipe_with_given_id() {
        // given
        RecipeDTO recipeDTO1 = new RecipeDTO(1, "Chicken Wings");
        RecipeDTO recipeDTO2 = new RecipeDTO(2, "Vegetarian Kebab");

        given(recipeClient.recipeById(2)).willReturn(recipeDTO2);

        // when
        RecipeDTO recipeById = recipeService.recipeById(2);

        // then
        assertThat(recipeById.getTitle(), is(recipeDTO2.getTitle()));
        assertThat(recipeById, is(recipeDTO2));
        assertThat(recipeById.getTitle(), not("Chicken Wings"));
        assertThat(recipeById, not(recipeDTO1));
    }

    @Test
    void should_return_meal_preparation_instructions_giving_id() {
        // given
        PreparationInstructionsDTO prep1 = new PreparationInstructionsDTO();
        PreparationInstructionsDTO prep2 = new PreparationInstructionsDTO();

        InstructionStepsDTO inst1 = new InstructionStepsDTO();
        InstructionStepsDTO inst2 = new InstructionStepsDTO();

        inst1.setNumber(1);
        inst1.setStep("Wash");
        List<InstructionStepsDTO> instructionStepsDTOList1 = List.of(inst1);

        inst2.setNumber(2);
        inst2.setStep("Cut");
        List<InstructionStepsDTO> instructionStepsDTOList2 = List.of(inst2);

        prep1.setSteps(instructionStepsDTOList1);
        prep2.setSteps(instructionStepsDTOList2);

        List<PreparationInstructionsDTO> prepList = Arrays.asList(prep1, prep2);


        given(recipeClient.preparationInstructions(1)).willReturn(prepList);

        //when
        List<PreparationInstructionsDTO> preparationInstructionsDTOS = recipeService.preparationInstructionsByRecipeId(1);

        //then
        assertThat(preparationInstructionsDTOS, is(prepList));
        assertThat(preparationInstructionsDTOS.get(0).getSteps().get(0).getStep(), is("Wash"));
        assertThat(preparationInstructionsDTOS.get(1).getSteps().get(0).getStep(), is(inst2.getStep()));
    }
}