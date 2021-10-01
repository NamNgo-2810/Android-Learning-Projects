package com.example.cheat;

public class ModelClass {

    String message;
    String from;

    public ModelClass(String message, String from) {
        this.message = message;
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public String getFrom() {
        return from;
    }
}
