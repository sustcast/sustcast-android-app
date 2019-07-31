package com.chameleon.sustcast.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class outer {


    @SerializedName("output")
    @Expose
    private Output Output;

    public Output getOutput() {
        return Output;
    }

    public void setOutput(Output output) {
        this.Output = output;
    }
}
