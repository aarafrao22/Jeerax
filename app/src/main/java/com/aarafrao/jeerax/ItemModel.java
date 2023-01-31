package com.aarafrao.jeerax;

public class ItemModel {
    private String txtName;
    private String password;

    public ItemModel(String txtName, String password) {
        this.txtName = txtName;
        this.password = password;
    }

    public String getTxtName() {
        return txtName;
    }

    public void setTxtName(String txtName) {
        this.txtName = txtName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
