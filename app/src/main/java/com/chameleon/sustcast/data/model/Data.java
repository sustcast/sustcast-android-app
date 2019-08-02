package com.chameleon.sustcast.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("song")
    @Expose
    private String song;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("secret")
    @Expose
    private String secret;
    @SerializedName("artist")
    @Expose
    private String artist;

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
