package com.example.kkRecipes.exception;

public class UserNotExistException extends RuntimeException{

    public UserNotExistException(String message) {
        super(message);
    }
}
