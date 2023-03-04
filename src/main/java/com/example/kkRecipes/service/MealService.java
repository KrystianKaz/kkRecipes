package com.example.kkRecipes.service;

import com.example.kkRecipes.model.Meal;
import com.example.kkRecipes.model.User;
import com.example.kkRecipes.repository.MealRepository;
import com.example.kkRecipes.webclient.RecipeClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class MealService {

    private final MealRepository mealRepository;
    private final RecipeClient recipeClient;

    public void addMealToLikedByUser(int id, User user, Meal meal) {
        meal.setMealId(id);
        meal.setMealTitle(recipeClient.recipeById(Math.toIntExact(id)).getTitle());
        meal.setUser(user);
        mealRepository.save(meal);
    }

    public void removeMealFromLikedByUser(int id, User user, Meal meal) {

    }

    public List<Meal> showUsersLikedMeals(User user) {
        return mealRepository.findAll()
                .stream()
                .filter(u -> u.getUser().equals(user))
                .toList();
    }
}
