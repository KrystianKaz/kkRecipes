package com.example.kkRecipes.service;

import com.example.kkRecipes.model.DailyDiet;
import com.example.kkRecipes.model.User;
import com.example.kkRecipes.model.dto.meal_plan.MealPlanDTO;
import com.example.kkRecipes.repository.DailyDietRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class DailyDietService {

    private final DailyDietRepository dailyDietRepository;

    public void saveDailyDiet(DailyDiet dailyDiet, MealPlanDTO mealPlanDTO, User user) {
        dailyDiet.setCalories(mealPlanDTO.getNutrients().getCalories());
        dailyDiet.setFat(mealPlanDTO.getNutrients().getFat());
        dailyDiet.setCarbohydrates(mealPlanDTO.getNutrients().getCarbohydrates());
        dailyDiet.setProtein(mealPlanDTO.getNutrients().getProtein());
        dailyDiet.setFirstMealId(mealPlanDTO.getMeals().get(0).getId());
        dailyDiet.setSecondMealId(mealPlanDTO.getMeals().get(1).getId());
        dailyDiet.setThirdMealId(mealPlanDTO.getMeals().get(2).getId());
        dailyDiet.setAddedByUser(user.getId());
        dailyDietRepository.save(dailyDiet);
    }

    public List<DailyDiet> findSavedDietsByUserAndSortByOldest(User user) {
        return dailyDietRepository.findAll().stream()
                .filter(u -> u.getAddedByUser() == user.getId())
                .sorted(Comparator.comparing(DailyDiet::getDate))
                .collect(Collectors.toList());
    }
}
