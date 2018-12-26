package com.entigrity.model.viewprofile;

import com.google.gson.annotations.SerializedName;


public class Payload {

    @SerializedName("data")
    private Data data;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    private String access_token;

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
                        "data = '" + data + '\'' +
                        "access_token = '" + access_token +
                        "}";
    }
}