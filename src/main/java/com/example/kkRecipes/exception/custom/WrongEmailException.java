package com.example.kkRecipes.exception.custom;

public class WrongEmailException extends RuntimeException{

    public WrongEmailException(String data) {
        super("Given mail: " + data + " is wrong");
    }
}
