package com.example.kkRecipes.exception;

public class CreatedUserExistException extends RuntimeException{

    public CreatedUserExistException(String data) {
        super("User with given data: " + data + " is already registered!");
    }
}
