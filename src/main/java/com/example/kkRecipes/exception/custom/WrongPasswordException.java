package com.example.kkRecipes.exception.custom;

public class WrongPasswordException extends RuntimeException{

    public WrongPasswordException() {
        super("Given password is blank!");
    }
}
