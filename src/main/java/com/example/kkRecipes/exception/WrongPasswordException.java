package com.example.kkRecipes.exception;

public class WrongPasswordException extends RuntimeException{

    public WrongPasswordException() {
        super("Given password is blank!");
    }
}
