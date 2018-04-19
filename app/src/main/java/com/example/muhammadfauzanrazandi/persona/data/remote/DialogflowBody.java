package com.example.muhammadfauzanrazandi.persona.data.remote;

public class DialogflowBody {
    private String contexts;
    private String lang;
    private String query;
    private String sessionId;

    public DialogflowBody(String contexts, String lang, String query, String sessionId) {
        this.contexts = contexts;
        this.lang = lang;
        this.query = query;
        this.sessionId = sessionId;
    }
}
