package com.example.kkRecipes.model;

import com.example.kkRecipes.model.enums.UserRolesEnum;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String email;

    private String username;

    private String password;

    private boolean active = true;

    private LocalTime accountCreationTime;

    private LocalDate accountCreationDate;

    @Column(columnDefinition = "ENUM('ADMIN', 'MODERATOR', 'USER')")
    @Enumerated(EnumType.STRING)
    private UserRolesEnum userRoles = UserRolesEnum.USER;

    @JoinColumn(name = "likedMeals", insertable = false, updatable = false)
    @ManyToMany
    private List<Meal> likedMeals;
}
