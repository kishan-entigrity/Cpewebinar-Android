package com.entigrity.model.viewprofile;

import com.google.gson.annotations.SerializedName;


public class Payload {

    @SerializedName("data")
    private Data data;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @SerializedName("access_token")

    private String accessToken;

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    @Override
    public String toString() {
        return
                "Payload{" +
                        "access_token = '" + accessToken + '\'' +
                        "data = '" + data + '\'' +
                        "}";
    }
}