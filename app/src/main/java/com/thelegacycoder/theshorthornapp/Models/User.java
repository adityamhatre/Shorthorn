package com.thelegacycoder.theshorthornapp.Models;

/**
 * Created by Aditya on 028, 28 Apr, 2017.
 */

public class User {
    String type;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public User(String type) {
        this.type = type;
    }
}
