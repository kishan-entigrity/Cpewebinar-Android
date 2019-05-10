package com.entigrity.webservice;

public class ApiUtils {
    private ApiUtils() {

    }

    public static final String BASE_URL = "https://my-cpe.com/api/";


    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL)
                .create(APIService.class);
    }


}
