package com.example.billspliter;

import android.view.View;

public class Model {
    String name;
    String number;
    boolean isSelected;

    public Model(String name, String number) {
        this.name = name;
        this.number = number;
        this.isSelected=false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


}
