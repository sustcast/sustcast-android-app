package com.chameleon.sustcast.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Icestats {

    @SerializedName("admin")
    @Expose
    private String admin;
    @SerializedName("host")
    @Expose
    private String host;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("server_id")
    @Expose
    private String serverId;
    @SerializedName("server_start")
    @Expose
    private String serverStart;
    @SerializedName("server_start_iso8601")
    @Expose
    private String serverStartIso8601;
    @SerializedName("source")
    @Expose
    private Source source;

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getServerStart() {
        return serverStart;
    }

    public void setServerStart(String serverStart) {
        this.serverStart = serverStart;
    }

    public String getServerStartIso8601() {
        return serverStartIso8601;
    }

    public void setServerStartIso8601(String serverStartIso8601) {
        this.serverStartIso8601 = serverStartIso8601;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

}