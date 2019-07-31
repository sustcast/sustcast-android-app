package com.chameleon.sustcast.data.remote;

public class ApiUtils {
    public static final String BASE_URL = "http://103.84.159.230:5080/api/";
    public static final String BASE_CURRENT = "http://103.84.159.230:5050/";

    public static UserClient getAPIService() {
        System.out.println("ApiUtils e asi");
        return RetrofitClient.getClient(BASE_URL).create(UserClient.class);
    }


    public static CurrentClient getMetadataService() {
        System.out.println("metadata service chole");
        return RetrofitCurrentClient.getMeta(BASE_CURRENT).create(CurrentClient.class);
    }
}
