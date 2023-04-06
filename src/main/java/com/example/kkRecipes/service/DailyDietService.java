package com.example.kkRecipes.service;

import com.example.kkRecipes.exception.custom.IllegalOperationException;
import com.example.kkRecipes.model.DailyDiet;
import com.example.kkRecipes.model.User;
import com.example.kkRecipes.model.dto.meal_plan.MealPlanDTO;
import com.example.kkRecipes.repository.DailyDietRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class DailyDietService {

    private final DailyDietRepository dailyDietRepository;
    private final UserService userService;

    public void saveDailyDiet(DailyDiet dailyDiet, MealPlanDTO mealPlanDTO, User user) {
        dailyDiet.setCalories(mealPlanDTO.getNutrients().getCalories());
        dailyDiet.setFat(mealPlanDTO.getNutrients().getFat());
        dailyDiet.setCarbohydrates(mealPlanDTO.getNutrients().getCarbohydrates());
        dailyDiet.setProtein(mealPlanDTO.getNutrients().getProtein());
        dailyDiet.setFirstMealId(mealPlanDTO.getMeals().get(0).getId());
        dailyDiet.setSecondMealId(mealPlanDTO.getMeals().get(1).getId());
        dailyDiet.setThirdMealId(mealPlanDTO.getMeals().get(2).getId());
        dailyDiet.setAddedByUser(user.getId());
        dailyDiet.setCompleted(false);
        dailyDietRepository.save(dailyDiet);
    }

    public Page<DailyDiet> findSavedUserDietsAndPageOrSortThem(Pageable pageable,
                                                               User user,
                                                               boolean isCompleted,
                                                               boolean reverseOrder) {
        Sort sort = Sort.by("date");
        if (reverseOrder) {
            sort = sort.descending();
        } else sort.ascending();

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        return dailyDietRepository.findAllByAddedByUserAndCompleted(user.getId(), isCompleted, sortedPageable);
    }

    public void changeDailyMealPlanCompletionStatus(long dailyPlanId, User user) {
        DailyDiet dailyDiet = getDailyDiet(dailyPlanId);

        if (dailyDiet.getAddedByUser() == user.getId()) {
            dailyDiet.setCompleted(!dailyDiet.isCompleted());
            dailyDietRepository.save(dailyDiet);
        } else throw new IllegalOperationException();
    }

    public void deleteDailyMealPlan(long dailyPlanId, User user) {
        DailyDiet dailyDiet = getDailyDiet(dailyPlanId);

        if (dailyDiet.getAddedByUser() == user.getId()) {
            dailyDietRepository.delete(dailyDiet);
        } else throw new IllegalOperationException();
    }

    public List<DailyDiet> findAllDietsSavedByUser(String username) {
        User userByUsername = userService.findUserByUsername(username);

        return dailyDietRepository.findAllByAddedByUser(userByUsername.getId());
    }


    private DailyDiet getDailyDiet(final long id) {
        return dailyDietRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("Daily diet with given ID not found!"));
    }
}
