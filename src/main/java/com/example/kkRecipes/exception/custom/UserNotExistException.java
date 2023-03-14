package com.example.kkRecipes.exception.custom;

public class UserNotExistException extends RuntimeException{

    public UserNotExistException(String username) {
        super("User with username: " + username + " not exists!");
    }
}
