package com.chameleon.sustcast.data.remote;

import com.chameleon.sustcast.data.model.OuterXSL;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface IceClient {
    @Headers("Content-Type:application/json")
    @GET("status-json.xsl")
    Call<OuterXSL> fetch();
}
