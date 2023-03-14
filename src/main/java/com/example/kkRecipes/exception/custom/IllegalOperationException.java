package com.example.kkRecipes.exception.custom;

public class IllegalOperationException extends IllegalArgumentException{
    public IllegalOperationException() {
        super("You cannot change other user's data!");
    }
}
