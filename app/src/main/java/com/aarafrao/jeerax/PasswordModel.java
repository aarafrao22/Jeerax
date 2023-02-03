package com.aarafrao.jeerax;

public class PasswordModel {
    String email, password, app, hashed;


    public PasswordModel(String email, String password, String app, String hashed) {
        this.email = email;
        this.password = password;
        this.app = app;
        this.hashed = hashed;
    }

    public String getHashed() {
        return hashed;
    }

    public void setHashed(String hashed) {
        this.hashed = hashed;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public PasswordModel() {
    }

}
