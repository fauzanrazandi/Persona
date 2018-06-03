package com.example.muhammadfauzanrazandi.persona.data.remote;

import com.example.muhammadfauzanrazandi.persona.data.model.Dialogflow;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface DFService {

    @Headers("Authorization: Bearer <USE YOUR OWN DEVELOPER DIALOGFLOW TOKEN HERE>")
    @GET("query?v=20150910&lang=id&sessionId=12345")
    Call<Dialogflow> getSpeech(@Query("query") String query);

}
