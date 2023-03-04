package com.example.kkRecipes.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int mealId;
    private String mealTitle;

    @JoinColumn(name = "liked_by_user")
    @ManyToOne
    private User user;
}
