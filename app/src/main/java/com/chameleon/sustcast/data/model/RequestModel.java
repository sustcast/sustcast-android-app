package com.chameleon.sustcast.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestModel {
    @SerializedName("secret")
    @Expose
    private String secret;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("song")
    @Expose
    private String song;
    @SerializedName("artist")
    @Expose
    private String artist;

    public RequestModel(String secret, String name, String song, String artist) {
        this.secret =  secret;
        this.name = name;
        this.song = song;
        this.artist = artist;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }


}
