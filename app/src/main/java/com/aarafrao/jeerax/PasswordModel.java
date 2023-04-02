package com.aarafrao.jeerax;

public class PasswordModel {
    String email, password, app, encrypted, date;

    public PasswordModel(String email, String password, String app, String encrypted, String date) {
        this.email = email;
        this.password = password;
        this.app = app;
        this.encrypted = encrypted;
        this.date = date;
    }

    public String getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(String encrypted) {
        this.encrypted = encrypted;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
