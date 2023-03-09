package com.example.kkRecipes.exception;

public class WrongEmailException extends RuntimeException{

    public WrongEmailException(String data) {
        super("Given mail: " + data + " is wrong");
    }
}
