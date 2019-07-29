package com.chameleon.sustcast.data.remote;

import com.chameleon.sustcast.data.model.OuterCurrent;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface CurrentClient {
    @Headers("Content-Type:application/json")
    @GET("current")
    Call<OuterCurrent> fetch();
}
