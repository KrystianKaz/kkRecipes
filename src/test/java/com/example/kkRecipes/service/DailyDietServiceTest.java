package com.example.kkRecipes.service;

import com.example.kkRecipes.exception.custom.IllegalOperationException;
import com.example.kkRecipes.model.DailyDiet;
import com.example.kkRecipes.model.User;
import com.example.kkRecipes.model.dto.meal_plan.MealPlanDTO;
import com.example.kkRecipes.model.dto.meal_plan.MealPlanNutrientsDTO;
import com.example.kkRecipes.model.dto.meal_plan.MealPlanValuesDTO;
import com.example.kkRecipes.repository.DailyDietRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DailyDietServiceTest {

    @InjectMocks
    private DailyDietService dailyDietService;

    @Mock
    private DailyDietRepository dailyDietRepository;

    @Mock
    private UserService userService;

    @Test
    void should_save_daily_diet_into_liked_by_user() {
        // given
        User user = getUser();

        MealPlanDTO mealPlan = getMealPlanDTO();

        DailyDiet dailyDiet = new DailyDiet();

        // when
        when(dailyDietRepository.save(any(DailyDiet.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        dailyDietService.saveDailyDiet(dailyDiet, mealPlan, user);

        // then
        then(dailyDietRepository).should().save(dailyDiet);
    }

    @Test
    void should_find_diets_liked_by_user_and_be_able_to_page_or_sort_them() {
        // given
        User user = getUser();

        DailyDiet dailyDiet1 = new DailyDiet();
        dailyDiet1.setAddedByUser(user.getId());
        dailyDiet1.setDate(LocalDate.of(2023, 3, 1));
        dailyDiet1.setFirstMealId(1);
        dailyDiet1.setSecondMealId(2);
        dailyDiet1.setThirdMealId(3);

        DailyDiet dailyDiet2 = new DailyDiet();
        dailyDiet2.setAddedByUser(user.getId());
        dailyDiet2.setDate(LocalDate.of(2023, 3, 10));
        dailyDiet2.setFirstMealId(4);
        dailyDiet2.setSecondMealId(5);
        dailyDiet2.setThirdMealId(6);

        boolean isCompleted = false;

        boolean reverseOrder = false;

        Pageable pageable = PageRequest.of(0, 1);

        List<DailyDiet> dietsList = List.of(dailyDiet1, dailyDiet2);
        Page<DailyDiet> page = new PageImpl<>(dietsList, pageable, dietsList.size());

        // when
        when(dailyDietService.findSavedUserDietsAndPageOrSortThem(pageable, user, isCompleted, reverseOrder))
                .thenReturn(page);

        Page<DailyDiet> userDiets = dailyDietService
                .findSavedUserDietsAndPageOrSortThem(pageable, user, isCompleted, reverseOrder);

        // then
        assertThat(userDiets.getTotalElements(), is(2L));
        assertThat(userDiets.getTotalPages(), is(2));
    }

    @Test
    void should_throw_exception_when_data_with_given_id_not_found() {
        // given
        User user = getUser();

        String message = "Daily diet with given ID not found!";

        // when
        Throwable exception = assertThrows(NullPointerException.class,
                () -> dailyDietService.changeDailyMealPlanCompletionStatus(1, user));

        // then
        assertThat(exception.getMessage(), is(new NullPointerException(message).getMessage()));
    }

    @Test
    void should_be_able_to_change_diet_completion_status() {
        // given
        User user = getUser();

        DailyDiet diet1 = getDailyDiet(1L, user.getId(), false);
        DailyDiet diet2 = getDailyDiet(2L, user.getId(), true);

        given(dailyDietRepository.findById(diet1.getId())).willReturn(Optional.of(diet1));
        given(dailyDietRepository.findById(diet2.getId())).willReturn(Optional.of(diet2));

        // when
        dailyDietService.changeDailyMealPlanCompletionStatus(diet1.getId(), user);
        dailyDietService.changeDailyMealPlanCompletionStatus(diet2.getId(), user);

        // then
        assertThat(diet1.isCompleted(), is(true));
        assertThat(diet2.isCompleted(), is(false));
    }

    @Test
    void should_throw_exception_when_given_user_tries_to_change_other_user_diet_status() {
        // given
        User user = getUser();

        DailyDiet diet = getDailyDiet(1L, 2L, false);

        given(dailyDietRepository.findById(diet.getId())).willReturn(Optional.of(diet));

        // when
        Throwable exception = assertThrows(IllegalOperationException.class,
                () -> dailyDietService.changeDailyMealPlanCompletionStatus(diet.getId(), user));

        // then
        assertThat(exception.getMessage(), is(new IllegalOperationException().getMessage()));
    }

    @Test
    void should_be_able_to_delete_meal_plan_from_saved_by_user() {
        // given
        User user = getUser();
        DailyDiet dailyDiet = getDailyDiet(1, 1L, true);

        given(dailyDietRepository.findById(dailyDiet.getId())).willReturn(Optional.of(dailyDiet));

        // when
        dailyDietService.deleteDailyMealPlan(dailyDiet.getId(), user);

        // then
        then(dailyDietRepository).should().delete(dailyDiet);
    }

    @Test
    void should_throw_exception_when_trying_to_delete_other_user_daily_plan() {
        // given
        User user = getUser();
        DailyDiet dailyDiet = getDailyDiet(1, 2L, false);

        given(dailyDietRepository.findById(dailyDiet.getId())).willReturn(Optional.of(dailyDiet));

        // when
        IllegalOperationException exception = assertThrows(IllegalOperationException.class,
                () -> dailyDietService.deleteDailyMealPlan(dailyDiet.getId(), user));

        // then
        assertThat(exception.getMessage(), is(new IllegalOperationException().getMessage()));
    }

    @Test
    void should_return_all_daily_diets_saved_by_user() {
        // given
        User givenUser = getUser();

        DailyDiet dailyDiet1 = getDailyDiet(1, givenUser.getId(), true);
        DailyDiet dailyDiet2 = getDailyDiet(2, givenUser.getId(), true);
        DailyDiet dailyDiet3 = getDailyDiet(3, givenUser.getId(), true);
        List<DailyDiet> savedDietsByUser = List.of(dailyDiet1, dailyDiet2, dailyDiet3);

        given(userService.findUserByUsername(givenUser.getUsername())).willReturn(givenUser);
        given(dailyDietRepository.findAllByAddedByUser(givenUser.getId())).willReturn(savedDietsByUser);

        // when
        List<DailyDiet> dietsSavedByUserAndFound = dailyDietService.findAllDietsSavedByUser(givenUser.getUsername());

        // then
        assertThat(dietsSavedByUserAndFound.size(), is(3));
        assertThat(dietsSavedByUserAndFound.get(1).getId(), is(2L));
        assertThat(dietsSavedByUserAndFound.get(2).getAddedByUser(), is(givenUser.getId()));
    }


    private static User getUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("TestUser");
        return user;
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


    private static DailyDiet getDailyDiet(long dietId, long userId, boolean isCompleted) {
        DailyDiet diet = new DailyDiet();
        diet.setAddedByUser(userId);
        diet.setId(dietId);
        diet.setCompleted(isCompleted);
        return diet;
    }
}