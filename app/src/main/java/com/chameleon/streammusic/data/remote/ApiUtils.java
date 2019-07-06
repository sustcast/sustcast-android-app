package com.chameleon.streammusic.data.remote;

public class ApiUtils {
    public static final String BASE_URL = "http://10.100.167.164/api/";

    public static UserClient getAPIService() {
        System.out.println("ApiUtils e asi");
        return RetrofitClient.getClient(BASE_URL).create(UserClient.class);
    }
}
