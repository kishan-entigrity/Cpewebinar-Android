package com.entigrity.webservice;

public class ApiUtils {
    private ApiUtils() {

    }

    public static final String BASE_URL = "http://admin.spaarg.tech/api/";


    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL)
                .create(APIService.class);
    }


}
