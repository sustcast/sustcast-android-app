package com.chameleon.streammusic.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class logoutResponse {

    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
