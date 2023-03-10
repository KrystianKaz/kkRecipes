package com.example.kkRecipes.model;

import com.example.kkRecipes.model.enums.UserRolesEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
@Component
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, length = 30, nullable = false)
    private String email;
    @NotBlank
    @Column(unique = true, length = 15, nullable = false)
    private String username;
    @NotBlank
    @Column(length = 60, nullable = false)
    private String password;

    private boolean active;

    private LocalTime accountCreationTime;

    private LocalDate accountCreationDate;

    @Column(columnDefinition = "ENUM('ADMIN', 'MODERATOR', 'USER')")
    @Enumerated(EnumType.STRING)
    private UserRolesEnum userRoles = UserRolesEnum.USER;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Meal> likedMeals;

    public User(final String username, final String password, final String email, final boolean active) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.active = active;
    }
}
