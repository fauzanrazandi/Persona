package com.example.muhammadfauzanrazandi.persona.data.remote;

public class ApiUtils {

    public static final String BASE_URL = "https://api.dialogflow.com/v1/";

    public static DFService getDFService() {
        return RetrofitClient.getClient(BASE_URL).create(DFService.class);
    }
}