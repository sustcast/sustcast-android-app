package com.chameleon.sustcast.data.remote;

public class ApiUtils {
    //public static final String BASE_URL = "https://10.100.162.222/api/";
    public static final String BASE_URL = "http://103.84.159.230:8000/";

    public static UserClient getAPIService() {
        System.out.println("ApiUtils e asi");
        return RetrofitClient.getClient(BASE_URL).create(UserClient.class);
    }
//
//     public  static UserClient getApiService2(){
//        return RetrofitClient.getClient(BASE_URL_2).create(UserClient.class);
//     }
}
