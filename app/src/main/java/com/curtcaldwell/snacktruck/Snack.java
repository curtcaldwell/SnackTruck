
package com.curtcaldwell.snacktruck;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Snack {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("isVeggie")
    @Expose
    private boolean isVeggie;

    private boolean isChecked;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsVeggie() {
        return isVeggie;
    }

    public void setIsVeggie(boolean isVeggie) {
        this.isVeggie = isVeggie;
    }

    public boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

}
