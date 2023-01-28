package com.aarafrao.jeerax;

public class ItemModel {
    private String txtName;
    private int img;

    public ItemModel(String txtName, int img) {
        this.txtName = txtName;
        this.img = img;
    }

    public String getTxtName() {
        return txtName;
    }

    public void setTxtName(String txtName) {
        this.txtName = txtName;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
