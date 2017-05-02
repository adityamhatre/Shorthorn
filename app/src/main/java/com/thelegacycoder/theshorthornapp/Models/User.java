package com.thelegacycoder.theshorthornapp.Models;

/**
 * Created by Aditya on 028, 28 Apr, 2017.
 */

public class User {
    String user, name, type;

    public User(){}
    public User(String type, String name, String user) {
        this.user = user;
        this.name = name;
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
