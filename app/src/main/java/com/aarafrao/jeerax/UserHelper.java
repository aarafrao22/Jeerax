package com.aarafrao.jeerax;

public class UserHelper {
    String name, password, email;

    public UserHelper(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public UserHelper() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
