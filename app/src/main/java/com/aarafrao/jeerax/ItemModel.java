package com.aarafrao.jeerax;

public class ItemModel {
    private String app;
    private String password;

    public ItemModel(String app, String password) {
        this.app = app;
        this.password = password;
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
