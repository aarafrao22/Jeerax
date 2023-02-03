package com.aarafrao.jeerax;

public class UserHelper {
    String name, password, email, hashed;

    public UserHelper(String name, String password, String email, String hashed) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.hashed = hashed;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashed() {
        return hashed;
    }

    public void setHashed(String hashed) {
        this.hashed = hashed;
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
