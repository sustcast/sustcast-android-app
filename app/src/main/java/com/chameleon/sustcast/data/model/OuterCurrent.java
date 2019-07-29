package com.chameleon.sustcast.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OuterCurrent {
    @SerializedName("current")
    @Expose
    private List<Current> current = null;

    public List<Current> getCurrent() {
        return current;
    }

    public void setCurrent(List<Current> current) {
        this.current = current;
    }
}
