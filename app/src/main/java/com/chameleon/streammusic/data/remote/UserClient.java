package com.chameleon.streammusic.data.remote;

import com.chameleon.streammusic.data.model.logoutResponse;
import com.chameleon.streammusic.data.model.outer;
import com.chameleon.streammusic.data.model.signupResponse;
import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserClient {
    @FormUrlEncoded
    @Headers("X-Requested-With:XMLHttpRequest")
    @POST("register")
    Call<signupResponse> registerUser(@Field("name") String name,
                                      @Field("email") String email,
                                      @Field("password") String password,
                                      @Field("c_password") String c_password,
                                      @Field("registration_number") String registration_number,
                                      @Field("department") String department,
                                      @Field("contact_number") String contact_number);


    @FormUrlEncoded
    @Headers("X-Requested-With:XMLHttpRequest")
    @POST("login")
    Call<outer> signin(@Field("email") String email,
                       @Field("password") String password);

    @GET("logout")
    Call<logoutResponse> signout(@Header("Authorization") String token);

    @FormUrlEncoded
    @Headers("X-Requested-With:XMLHttpRequest")
    @POST("password/create")
    Call<JsonElement>requestReset(@Field("email") String email);

    @FormUrlEncoded
    @Headers("X-Requested-With:XMLHttpRequest")
    @POST("password/reset")
    Call<JsonElement>resetPassword(@Field("email") String email,
                                   @Field("password") String password,
                                   @Field("c_password") String c_password,
                                   @Field("token") String token);


}
