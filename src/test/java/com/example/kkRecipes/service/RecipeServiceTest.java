package com.example.kkRecipes.service;

import com.example.kkRecipes.model.dto.analyzed_instructions.InstructionStepsDTO;
import com.example.kkRecipes.model.dto.analyzed_instructions.PreparationInstructionsDTO;
import com.example.kkRecipes.model.dto.complex_search.ComplexSearchDTO;
import com.example.kkRecipes.model.dto.complex_search.ComplexSearchResultsDTO;
import com.example.kkRecipes.model.dto.complex_search.ComplexSearchValuesDTO;
import com.example.kkRecipes.model.dto.meal_plan.*;
import com.example.kkRecipes.model.dto.nutrients_search.NutrientsSearchResultsDTO;
import com.example.kkRecipes.model.dto.nutrients_search.NutrientsSearchValuesDTO;
import com.example.kkRecipes.model.dto.recipe.RecipeDTO;
import com.example.kkRecipes.webclient.RecipeClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
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

        given(recipeClient.recipeById(2))
                .willReturn(recipeDTO2);

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


        given(recipeClient.preparationInstructions(1))
                .willReturn(prepList);

        //when
        List<PreparationInstructionsDTO> preparationInstructionsDTOS =
                recipeService.preparationInstructionsByRecipeId(1);

        //then
        assertThat(preparationInstructionsDTOS, is(prepList));
        assertThat(preparationInstructionsDTOS.get(0).getSteps().get(0).getStep(), is("Wash"));
        assertThat(preparationInstructionsDTOS.get(1).getSteps().get(0).getStep(), is(inst2.getStep()));
    }

    @Test
    void should_return_recipes_by_giving_meal_details() {
        // given
        ComplexSearchValuesDTO searchValuesDTO = new ComplexSearchValuesDTO();
        searchValuesDTO.setCuisine("italian");

        given(recipeClient.complexSearch(searchValuesDTO))
                .willReturn(getRecipesByGivingMealDetails());

        // when
        ComplexSearchDTO results = recipeService.recipeComplexSearch(searchValuesDTO);

        // then
        assertThat(results.getResults().size(), is(3));
    }

    @Test
    void should_return_recipes_by_giving_nutritions() {
        // given
        NutrientsSearchValuesDTO searchValues = new NutrientsSearchValuesDTO();
        searchValues.setMinProtein(15);

        given(recipeClient.searchByNutrients(searchValues))
                .willReturn(getRecipesByGivingNutritionValues());

        // when
        List<NutrientsSearchResultsDTO> results = recipeService.recipeNutrientsSearch(searchValues);

        // then
        assertThat(results.size(), is(2));
    }

    @Test
    void should_generate_daily_meal_plan() {
        // given
        MealPlanDTO mealPlan = new MealPlanDTO();
        MealPlanValuesDTO meal1 = new MealPlanValuesDTO(1, "Sandwich with hummus paste");
        MealPlanValuesDTO meal2 = new MealPlanValuesDTO(2, "Spaghetti");
        MealPlanValuesDTO meal3 = new MealPlanValuesDTO(3, "Banana-protein shake");
        List<MealPlanValuesDTO> mealList = List.of(meal1, meal2, meal3);
        mealPlan.setMeals(mealList);

        MealPlanSearchValuesDTO searchValues = getMealPlanSearchValuesDTO();

        given(recipeClient.generateDailyMealPlan(searchValues))
                .willReturn(mealPlan);
        given(recipeClient.recipeById(1))
                .willReturn(new RecipeDTO("SandwichPhoto"));
        given(recipeClient.recipeById(2))
                .willReturn(new RecipeDTO("SpaghettiPhoto"));
        given(recipeClient.recipeById(3))
                .willReturn(new RecipeDTO("ShakePhoto"));

        // when
        MealPlanDTO results = recipeService.generateDailyMealPlan(searchValues);

        // then
        assertThat(results.getMeals().get(0).getTitle(), is(meal1.getTitle()));
    }

    @Test
    void should_generate_weekly_meal_plan() {
        // given
        MealPlanWeekDTO plan = getMealsForWeeklyPlan();

        given(recipeClient.generateWeeklyMealPlan(getMealPlanSearchValuesDTO()))
                .willReturn(plan);
        // when
        MealPlanWeekDTO mealPlanWeekDTO = recipeService
                .generateWeeklyMealPlan(getMealPlanSearchValuesDTO());

        // then
        assertThat(mealPlanWeekDTO.getWeek().getMonday().getMeals().size(),
                is(3));
        assertThat(mealPlanWeekDTO.getWeek().getThursday().getMeals().get(0),
                is(plan.getWeek().getThursday().getMeals().get(0)));
        assertThat(mealPlanWeekDTO.getWeek().getSunday().getMeals().get(2).getTitle(),
                is("Low Fat Potato Baked Oven Chips"));
    }

    private ComplexSearchDTO getRecipesByGivingMealDetails() {
        ComplexSearchDTO complexSearchDTO = new ComplexSearchDTO();
        List<ComplexSearchResultsDTO> results = new ArrayList<>();

        ComplexSearchResultsDTO meal1 = new ComplexSearchResultsDTO();
        meal1.setTitle("Tomato sauce with pasta");

        ComplexSearchResultsDTO meal2 = new ComplexSearchResultsDTO();
        meal2.setTitle("Bruschetta");

        ComplexSearchResultsDTO meal3 = new ComplexSearchResultsDTO();
        meal3.setTitle("Spaghetti carbonara");

        results.add(meal1);
        results.add(meal2);
        results.add(meal3);

        complexSearchDTO.setResults(results);
        return complexSearchDTO;
    }

    private List<NutrientsSearchResultsDTO> getRecipesByGivingNutritionValues() {
        List<NutrientsSearchResultsDTO> results = new ArrayList<>();

        NutrientsSearchResultsDTO meal1 =
                new NutrientsSearchResultsDTO("Protein pancakes", String.valueOf(18));

        NutrientsSearchResultsDTO meal2 =
                new NutrientsSearchResultsDTO("Protein shake", String.valueOf(25));

        results.add(meal1);
        results.add(meal2);
        return results;
    }

    private MealPlanSearchValuesDTO getMealPlanSearchValuesDTO() {
        MealPlanSearchValuesDTO searchValues = new MealPlanSearchValuesDTO();
        searchValues.setDiet(null);
        searchValues.setTargetCalories(2000);
        return searchValues;
    }

    private MealPlanWeekDTO getMealsForWeeklyPlan() {
        MealPlanWeekDTO weeklyPlan = new MealPlanWeekDTO();

        MealPlanValuesDTO meal1 = new MealPlanValuesDTO(1, "Sandwich with Hummus Paste");
        MealPlanValuesDTO meal2 = new MealPlanValuesDTO(2, "Turkey Tomato Cheese Pizza");
        MealPlanValuesDTO meal3 = new MealPlanValuesDTO(3, "Vegan Colcannon Soup");
        MealPlanValuesDTO meal4 = new MealPlanValuesDTO(4, "Boiled Egg sandwich");
        MealPlanValuesDTO meal5 = new MealPlanValuesDTO(5, "Vegetarian Mushroom Pie");
        MealPlanValuesDTO meal6 = new MealPlanValuesDTO(6, "Roma Tomato Bruschetta");
        MealPlanValuesDTO meal7 = new MealPlanValuesDTO(7, "Ham Toast");
        MealPlanValuesDTO meal8 = new MealPlanValuesDTO(8, "Lemony Greek Soup");
        MealPlanValuesDTO meal9 = new MealPlanValuesDTO(9, "Classic French Mussels");
        MealPlanValuesDTO meal10 = new MealPlanValuesDTO(10, "Protein shake");
        MealPlanValuesDTO meal11 = new MealPlanValuesDTO(11, "Italian Tuna Pasta");
        MealPlanValuesDTO meal12 = new MealPlanValuesDTO(12, "Eggplant Parmesan Roll-Ups");
        MealPlanValuesDTO meal13 = new MealPlanValuesDTO(13, "Banana smoothie");
        MealPlanValuesDTO meal14 = new MealPlanValuesDTO(14, "Salmon Quinoa Risotto");
        MealPlanValuesDTO meal15 = new MealPlanValuesDTO(15, "Pear and Pesto Crostini");
        MealPlanValuesDTO meal16 = new MealPlanValuesDTO(16, "Doughnuts");
        MealPlanValuesDTO meal17 = new MealPlanValuesDTO(17, "Baked Ratatouille");
        MealPlanValuesDTO meal18 = new MealPlanValuesDTO(18, "Cottage Cheese Pizza");
        MealPlanValuesDTO meal19 = new MealPlanValuesDTO(19, "Peanut Butter and Jelly Smoothie");
        MealPlanValuesDTO meal20 = new MealPlanValuesDTO(20, "Asparagus Lemon Risotto");
        MealPlanValuesDTO meal21 = new MealPlanValuesDTO(21, "Low Fat Potato Baked Oven Chips");

        List<MealPlanValuesDTO> monday = List.of(meal1, meal2, meal3);
        List<MealPlanValuesDTO> tuesday = List.of(meal4, meal5, meal6);
        List<MealPlanValuesDTO> wednesday = List.of(meal7, meal8, meal9);
        List<MealPlanValuesDTO> thursday = List.of(meal10, meal11, meal12);
        List<MealPlanValuesDTO> friday = List.of(meal13, meal14, meal15);
        List<MealPlanValuesDTO> saturday = List.of(meal16, meal17, meal18);
        List<MealPlanValuesDTO> sunday = List.of(meal19, meal20, meal21);

        MealPlanDTO mondayPlan = new MealPlanDTO();
        mondayPlan.setMeals(monday);

        MealPlanDTO tuesdayPlan = new MealPlanDTO();
        tuesdayPlan.setMeals(tuesday);

        MealPlanDTO wednesdayPlan = new MealPlanDTO();
        wednesdayPlan.setMeals(wednesday);

        MealPlanDTO thursdayPlan = new MealPlanDTO();
        thursdayPlan.setMeals(thursday);

        MealPlanDTO fridayPlan = new MealPlanDTO();
        fridayPlan.setMeals(friday);

        MealPlanDTO saturdayPlan = new MealPlanDTO();
        saturdayPlan.setMeals(saturday);

        MealPlanDTO sundayPlan = new MealPlanDTO();
        sundayPlan.setMeals(sunday);

        MealPlanDaysDTO days = new MealPlanDaysDTO();
        days.setMonday(mondayPlan);
        days.setTuesday(tuesdayPlan);
        days.setWednesday(wednesdayPlan);
        days.setThursday(thursdayPlan);
        days.setFriday(fridayPlan);
        days.setSaturday(saturdayPlan);
        days.setSunday(sundayPlan);

        weeklyPlan.setWeek(days);

        return weeklyPlan;
    }
}