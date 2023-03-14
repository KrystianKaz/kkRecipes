package com.example.kkRecipes.service;

import com.example.kkRecipes.model.Meal;
import com.example.kkRecipes.model.User;
import com.example.kkRecipes.model.dto.recipe.RecipeDTO;
import com.example.kkRecipes.repository.MealRepository;
import com.example.kkRecipes.webclient.RecipeClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MealServiceTest {

    @InjectMocks
    private MealService mealService;
    @Mock
    private MealRepository mealRepository;
    @Mock
    private RecipeClient recipeClient;

    @Test
    void should_save_meal_to_liked_by_user() {
        // given
        User user = getUser();

        RecipeDTO recipeDTO = new RecipeDTO(111999, "Sandwich");

        Meal meal = new Meal();
        meal.setMealId(recipeDTO.getId());

        given(recipeClient.recipeById(recipeDTO.getId()))
                .willReturn(recipeDTO);

        // when
        when(mealRepository.save(any(Meal.class)))
                .thenReturn(meal);

        mealService.addMealToLikedByUser(meal.getMealId(), user, meal);

        // then
        then(mealRepository).should().save(meal);
    }

    @Test
    void should_return_list_of_meal_liked_by_user() {
        // given
        User user = getUser();

        RecipeDTO recipeDTO1 = new RecipeDTO(123321123, "Bread");
        Meal meal1 = new Meal();
        meal1.setMealId(recipeDTO1.getId());
        meal1.setUser(user);

        RecipeDTO recipeDTO2 = new RecipeDTO(98753241, "Pizza");
        Meal meal2 = new Meal();
        meal2.setMealId(recipeDTO2.getId());
        meal2.setUser(user);

        List<Meal> likedMeals = List.of(meal1, meal2);

        // when
        when(mealService.mealsAddedByUserToLiked(user))
                .thenReturn(likedMeals);

        List<Meal> meals = mealService.mealsAddedByUserToLiked(user);

        // then
        assertThat(meals.size(), is(2));
        assertThat(meals.get(0).getMealTitle(), is(meal1.getMealTitle()));
    }

    @Test
    void should_return_true_when_meal_is_still_liked_by_user() {
        // given
        User user = getUser();

        Meal meal1 = getMeal(121, "Homemade Cheese");
        meal1.setUser(user);
        meal1.setStillLiked(true);

        Meal meal2 = getMeal(122, "Apple Pie");
        meal2.setUser(user);
        meal2.setStillLiked(false);

        Meal meal3 = getMeal(123, "Sandwich");
        meal3.setUser(user);
        meal3.setStillLiked(true);


        List<Meal> mealList = List.of(meal1, meal2, meal3);

        // when
        when(mealService.mealsAddedByUserToLiked(user))
                .thenReturn(mealList);

        boolean isMeal1StillLiked = mealService.isMealStillLikedByUser(mealList.get(0).getMealId(), user);
        boolean isMeal2StillLiked = mealService.isMealStillLikedByUser(mealList.get(1).getMealId(), user);
        boolean isMeal3StillLiked = mealService.isMealStillLikedByUser(mealList.get(2).getMealId(), user);

        // then
        assertThat(isMeal1StillLiked, is(true));
        assertThat(isMeal2StillLiked, is(false));
        assertThat(isMeal3StillLiked, is(true));
    }

    @Test
    void should_change_like_status_of_meal() {
        // given
        User user = getUser();

        Meal meal1 = getMeal(321, "Cheesecake");
        meal1.setUser(user);
        meal1.setStillLiked(true);

        Meal meal2 = getMeal(322, "Falafel");
        meal2.setUser(user);
        meal2.setStillLiked(false);

        List<Meal> mealList = List.of(meal1, meal2);

        // when
        when(mealService.mealsAddedByUserToLiked(user))
                .thenReturn(mealList);

        mealService.addOrRemoveMealFromLikedByUser(mealList.get(0).getMealId(), user);
        mealService.addOrRemoveMealFromLikedByUser(mealList.get(1).getMealId(), user);

        // then
        assertThat(mealList.get(0).isStillLiked(), is(false));
        assertThat(mealList.get(1).isStillLiked(), is(true));
    }

    @Test
    void should_return_true_when_meal_was_already_added_to_liked_by_user () {
        // given
        User user = getUser();

        Meal meal = getMeal(1000, "Caramel Popcorn");
        meal.setUser(user);

        List<Meal> mealList = List.of(meal);

        // when
        when(mealService.mealsAddedByUserToLiked(user))
                .thenReturn(mealList);
        boolean wasMealAddedToLikedByUser = mealService.wasMealAddedToLikedByUser(meal.getMealId(), user);

        // then
        assertThat(wasMealAddedToLikedByUser, is(true));
    }

    @Test
    void should_return_false_when_meal_was_not_added_to_liked_by_user () {
        // given
        User user = getUser();

        Meal notAddedMealToLiked = getMeal(1234, "Fried Chicken");

        Meal addedMealToLiked = getMeal(1233, "Vegetarian Burger");
        addedMealToLiked.setUser(user);

        List<Meal> likedMealsList = List.of(addedMealToLiked);

        // when
        when(mealService.mealsAddedByUserToLiked(user))
                .thenReturn(likedMealsList);
        boolean wasMealAddedToLikedByUser = mealService.wasMealAddedToLikedByUser(notAddedMealToLiked.getMealId(), user);

        // then
        assertThat(wasMealAddedToLikedByUser, is(false));
    }

    @Test
    void should_return_list_of_currently_liked_meals_by_user() {
        // given
        User user = getUser();

        Meal meal1 = getMeal(111, "Rice with Chicken");
        meal1.setUser(user);
        meal1.setStillLiked(true);

        Meal meal2 = getMeal(222, "Grilled Veggies");
        meal2.setUser(user);
        meal2.setStillLiked(false);

        List<Meal> mealList = List.of(meal1, meal2);

        // when
        when(mealService.showMealsCurrentlyLikedByUser(user))
                .thenReturn(mealList);

        List<Meal> results = mealService.showMealsCurrentlyLikedByUser(user);

        // then
        assertThat(results.size(), is(1));
    }

    private Meal getMeal(int id, String title) {
        RecipeDTO recipeDTO = new RecipeDTO(id, title);
        Meal meal = new Meal();
        meal.setMealId(recipeDTO.getId());
        return meal;
    }

    private static User getUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("TestUser");
        return user;
    }
}