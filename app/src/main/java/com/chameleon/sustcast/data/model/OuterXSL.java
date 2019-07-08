package com.chameleon.sustcast.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OuterXSL {
    @SerializedName("icestats")
    @Expose
    private Icestats icestats;

    public Icestats getIcestats() {
        return icestats;
    }

    public void setIcestats(Icestats icestats) {
        this.icestats = icestats;
    }

}
