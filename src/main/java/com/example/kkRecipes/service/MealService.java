package com.example.kkRecipes.service;

import com.example.kkRecipes.model.Meal;
import com.example.kkRecipes.model.User;
import com.example.kkRecipes.repository.MealRepository;
import com.example.kkRecipes.webclient.RecipeClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class MealService {

    private final MealRepository mealRepository;
    private final RecipeClient recipeClient;

    public void addRecipeToLikedByUser(int id, User user, Meal meal) {
        meal.setMealId(id);
        meal.setMealTitle(recipeClient.recipeById(Math.toIntExact(id)).getTitle());
        meal.setUser(user);
        mealRepository.save(meal);
    }
}
