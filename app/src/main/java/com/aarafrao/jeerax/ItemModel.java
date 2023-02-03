package com.aarafrao.jeerax;

public class ItemModel {
    private String app;
    private String password;
    private String hashed;


    public ItemModel() {

    }

    public ItemModel(String app, String password, String hashed) {
        this.app = app;
        this.password = password;
        this.hashed = hashed;
    }

    public String getHashed() {
        return hashed;
    }

    public void setHashed(String hashed) {
        this.hashed = hashed;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
