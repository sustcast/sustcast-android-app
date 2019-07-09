package com.chameleon.sustcast.data.remote;

public class ApiUtils {
    public static final String BASE_URL = "http://192.168.0.112/api/";
    public static final String BASE_ICE = "http://103.84.159.230:8000/";

    public static UserClient getAPIService() {
        System.out.println("ApiUtils e asi");
        return RetrofitClient.getClient(BASE_URL).create(UserClient.class);
    }

    public static IceClient getSongService(){
        System.out.println("songservice chole");
        return RetrofitIceClient.getIce(BASE_ICE).create(IceClient.class);
    }
}
