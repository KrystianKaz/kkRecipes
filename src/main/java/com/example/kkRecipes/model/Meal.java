package com.example.kkRecipes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;

import java.util.List;

@Entity
public class Meal {

    private int id;

    private String title;

    @JoinColumn(name = "likedByUser", insertable = false, updatable = false)
    @ManyToMany
    private List<User> likedBy;
}
