package com.example.muhammadfauzanrazandi.persona.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("errorType")
    @Expose
    private String errorType;
    @SerializedName("webhookTimedOut")
    @Expose
    private Boolean webhookTimedOut;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public Boolean getWebhookTimedOut() {
        return webhookTimedOut;
    }

    public void setWebhookTimedOut(Boolean webhookTimedOut) {
        this.webhookTimedOut = webhookTimedOut;
    }

}