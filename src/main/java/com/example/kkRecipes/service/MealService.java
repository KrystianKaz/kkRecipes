package com.example.kkRecipes.service;

import com.example.kkRecipes.model.Meal;
import com.example.kkRecipes.model.User;
import com.example.kkRecipes.repository.MealRepository;
import com.example.kkRecipes.webclient.RecipeClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class MealService {

    private final MealRepository mealRepository;
    private final RecipeClient recipeClient;

    public void addMealToLikedByUser(int mealId, User user, Meal meal) {
        meal.setMealId(mealId);
        meal.setMealTitle(recipeClient.recipeById(mealId).getTitle());
        meal.setUser(user);
        meal.setStillLiked(true);
        mealRepository.save(meal);
    }

    public List<Meal> mealsAddedByUserToLiked(User user) {
        return mealRepository.findAll()
                .stream()
                .filter(u -> u.getUser().equals(user))
                .collect(Collectors.toList());
    }

    public boolean isMealStillLikedByUser(int mealId, User user) {
        List<Meal> mealsLikedByUser = mealsAddedByUserToLiked(user);
        return mealsLikedByUser
                .stream()
                .filter(m -> m.getMealId() == mealId)
                .anyMatch(Meal::isStillLiked);
    }

    public void addOrRemoveMealFromLikedByUser(int mealId, User user) {
        List<Meal> mealsLikedByUser = mealsAddedByUserToLiked(user);
        Meal likedMeal = mealsLikedByUser
                .stream()
                .filter(m -> m.getMealId() == mealId)
                .findAny()
                .get();
        likedMeal.setStillLiked(!likedMeal.isStillLiked());
        mealRepository.save(likedMeal);
    }

    public boolean wasMealAddedToLikedByUser(int mealId, User user) {
        List<Meal> meals = mealsAddedByUserToLiked(user);
        return meals
                .stream()
                .anyMatch(meal -> meal.getMealId() == mealId);
    }

    public List<Meal> showMealsCurrentlyLikedByUser(User user) {
        return mealRepository.findAll()
                .stream()
                .filter(u -> u.getUser().equals(user))
                .filter(Meal::isStillLiked)
                .toList();
    }
}
