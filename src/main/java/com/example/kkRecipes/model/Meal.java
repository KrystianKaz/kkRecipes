package com.example.kkRecipes.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int mealId;
    private String mealTitle;

    @JoinColumn(name = "likedByUser", insertable = false, updatable = false)
    @ManyToMany
    private List<User> likedBy;
}
