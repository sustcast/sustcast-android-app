package com.chameleon.sustcast.data.remote;

import com.chameleon.sustcast.data.model.OuterCurrent;
import com.chameleon.sustcast.data.model.RequestModel;
import com.chameleon.sustcast.data.model.RequestResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CurrentClient {
    @Headers("Content-Type:application/json")
    @GET("current")
    Call<OuterCurrent> fetch();

    @POST("request")
    Call<RequestResponseModel> requestMusic(@Body RequestModel requestModel);


}
