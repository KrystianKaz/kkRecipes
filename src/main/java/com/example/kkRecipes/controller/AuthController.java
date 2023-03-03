package com.example.kkRecipes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class AuthController {

    @GetMapping("/")
    public String getHomePage() {
        return "index";
    }
}
