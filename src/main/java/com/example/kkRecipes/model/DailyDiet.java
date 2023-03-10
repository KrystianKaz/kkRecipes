package com.example.kkRecipes.model;

import com.example.kkRecipes.model.enums.DietTypesEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Setter
@Getter
public class DailyDiet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "ENUM('DAILY', 'WEEKLY')")
    @Enumerated(EnumType.STRING)
    private DietTypesEnum dietType = DietTypesEnum.DAILY;
    private LocalDate date;
    private String description;
    private int firstMealId;
    private int secondMealId;
    private int thirdMealId;
    private double calories;
    private double fat;
    private double carbohydrates;
    private double protein;
    private long addedByUser;
}
