package com.example.kkRecipes.controller;

import com.example.kkRecipes.model.User;
import com.example.kkRecipes.model.dto.complex_search.ComplexSearchValuesDTO;
import com.example.kkRecipes.model.dto.meal_plan.MealPlanDTO;
import com.example.kkRecipes.model.dto.meal_plan.MealPlanNutrientsDTO;
import com.example.kkRecipes.model.dto.meal_plan.MealPlanSearchValuesDTO;
import com.example.kkRecipes.model.dto.meal_plan.MealPlanValuesDTO;
import com.example.kkRecipes.model.dto.nutrients_search.NutrientsSearchValuesDTO;
import com.example.kkRecipes.model.dto.recipe.RecipeDTO;
import com.example.kkRecipes.repository.UserRepository;
import com.example.kkRecipes.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;
    @Mock
    private RecipeService recipeService;

    @Test
    void should_get_recipe_and_be_able_to_add_meal_to_liked_if_user_is_logged() throws Exception {
        // given
        User user = getUser();
        int id = 1459207;

        // when + then
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/recipes/" + id)
                        .with(user(user.getUsername())))
                .andExpect(status().is(200))
                .andExpect(view().name("result_pages/meal"))
                .andExpect(content().string(containsString("Easy Sheet Pan Pancakes")))
                .andExpect(model().attributeExists("wasMealAddedToLikedByUser", "stillLiked"));
    }

    @Test
    void should_be_able_to_search_recipes_by_giving_preferences() throws Exception {
        // given
        ComplexSearchValuesDTO complexSearchValuesDTO = new ComplexSearchValuesDTO();
        complexSearchValuesDTO.setQuery("pasta");
        complexSearchValuesDTO.setCuisine("Italian");

        // when + then
        mockMvc.perform(get("/complexSearch")
                        .flashAttr("complexSearchValuesDTO", complexSearchValuesDTO))
                .andExpect(status().is(200))
                .andExpect(model().attributeExists("meals"))
                .andExpect(content().string(containsString("Pasta")));
    }

    @Test
    void should_find_recipes_by_giving_nutrition_values() throws Exception {
        // given
        NutrientsSearchValuesDTO searchValues = getNutrientsSearchValuesDTO();

        // when + then
        mockMvc.perform(get("/foundByNutrients")
                        .flashAttr("nutrientsSearchValuesDTO", searchValues))
                .andExpect(status().is(200))
                .andExpect(model().attributeExists("nutrients"))
                .andExpect(model().attribute("nutrients", not(empty())))
                .andExpect(model().attribute("nutrients", hasSize(greaterThan(1))));
    }


    @Test
    void should_return_empty_list_of_meals_by_not_giving_parameters() throws Exception {
        // given
        NutrientsSearchValuesDTO nutrientsSearchValuesDTO = new NutrientsSearchValuesDTO();

        // when + then
        mockMvc.perform(get("/foundByNutrients")
                        .flashAttr("nutritionSearchValuesDTO", nutrientsSearchValuesDTO))
                .andExpect(status().is(200))
                .andExpect(model().attribute("nutrients", is(empty())))
                .andExpect(content().string(containsString("No matching meals found")));
    }

    @Test
    void should_be_able_to_generate_daily_meal_plan() throws Exception {
        // given
        MealPlanSearchValuesDTO searchValues = getMealPlanSearchValuesDTO();

        // when + then
        mockMvc.perform(get("/dailyMealPlan")
                        .flashAttr("mealPlanSearchValuesDTO", searchValues))
                .andExpect(status().is(200))
                .andExpect(model().attribute("plan", hasProperty("meals", hasSize(3))))
                .andExpect(model().attribute("plan",
                        hasProperty("nutrients",
                                hasProperty("calories", greaterThan(1900.0)))))
                .andExpect(model().attribute("plan",
                        hasProperty("nutrients",
                                hasProperty("calories", lessThan(2100.0)))))
                .andExpect(content().string(not(containsString("chicken"))))
                .andExpect(content().string(not(containsString("meat"))));
    }

    @Test
    @WithMockUser(username = "test", roles = "USER", authorities = "USER")
    @Transactional
    void should_be_able_to_save_meal_plan() throws Exception {
        // given
        User user = getUser();
        MealPlanDTO mealPlanDTO = getMealPlanDTO();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("plan", mealPlanDTO);

        // when + then
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/saveDailyMealPlan")
                        .session(session)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/savedDailyDiets"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("savedDiets"))
                .andExpect(model().attribute("savedDiets",
                        hasItem(hasProperty("firstMealId", is(1)))))
                .andExpect(model().attribute("savedDiets",
                        hasItem(hasProperty("addedByUser", is(user.getId())))));
    }

    private static MealPlanSearchValuesDTO getMealPlanSearchValuesDTO() {
        MealPlanSearchValuesDTO mealPlanSearchValuesDTO = new MealPlanSearchValuesDTO();
        mealPlanSearchValuesDTO.setTargetCalories(2000);
        mealPlanSearchValuesDTO.setDiet("Vegetarian");
        return mealPlanSearchValuesDTO;
    }

    private static NutrientsSearchValuesDTO getNutrientsSearchValuesDTO() {
        NutrientsSearchValuesDTO nutrientsSearchValuesDTO = new NutrientsSearchValuesDTO();
        nutrientsSearchValuesDTO.setMinCalories(800);
        nutrientsSearchValuesDTO.setMaxCalories(1000);
        nutrientsSearchValuesDTO.setMinFat(20);
        nutrientsSearchValuesDTO.setMaxFat(30);
        nutrientsSearchValuesDTO.setMinCarbs(80);
        nutrientsSearchValuesDTO.setMaxCarbs(100);
        nutrientsSearchValuesDTO.setMinProtein(40);
        nutrientsSearchValuesDTO.setMaxProtein(60);
        return nutrientsSearchValuesDTO;
    }

    private static MealPlanDTO getMealPlanDTO() {
        MealPlanDTO mealPlanDTO = new MealPlanDTO();

        MealPlanNutrientsDTO nutrients = new MealPlanNutrientsDTO();
        nutrients.setCalories(2000);
        nutrients.setFat(60);
        nutrients.setCarbohydrates(250);
        nutrients.setProtein(110);
        mealPlanDTO.setNutrients(nutrients);

        MealPlanValuesDTO meal1 = new MealPlanValuesDTO(1, "Burger");
        MealPlanValuesDTO meal2 = new MealPlanValuesDTO(2, "Pizza");
        MealPlanValuesDTO meal3 = new MealPlanValuesDTO(3, "Cheesecake");
        List<MealPlanValuesDTO> mealPlanValuesDTOList = List.of(meal1, meal2, meal3);
        mealPlanDTO.setMeals(mealPlanValuesDTOList);

        return mealPlanDTO;
    }

    private static User getUser() {
        return new User("test", "test", "test@test.com", true);
    }
}