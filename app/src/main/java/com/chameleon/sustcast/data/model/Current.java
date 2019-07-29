package com.chameleon.sustcast.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Current {

    @SerializedName("genre")
    @Expose
    private String genre;
    @SerializedName("song")
    @Expose
    private String song;
    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("artist")
    @Expose
    private String artist;
    @SerializedName("lyric")
    @Expose
    private String lyric;
    @SerializedName("filepath")
    @Expose
    private String filepath;
    @SerializedName("queue")
    @Expose
    private Integer queue;
    @SerializedName("progress")
    @Expose
    private Double progress;

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public Integer getQueue() {
        return queue;
    }

    public void setQueue(Integer queue) {
        this.queue = queue;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

}