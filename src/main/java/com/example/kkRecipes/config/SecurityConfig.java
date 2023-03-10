package com.example.kkRecipes.config;

import com.example.kkRecipes.service.MyUserDetailsService;
import com.example.kkRecipes.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    MyUserDetailsService detailsService() {
        return new MyUserDetailsService(userService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/user", "/likedMeals", "/savedDailyDiets").hasAnyAuthority("USER", "ADMIN")
//                        .requestMatchers("/search").hasAnyAuthority("ADMIN")
                        .anyRequest().permitAll()
                )
                .csrf().disable()
                .formLogin()
                .loginPage("/login")
                .and()
                .logout()
                .and()
                .userDetailsService(detailsService());
        return http.build();
    }
}

