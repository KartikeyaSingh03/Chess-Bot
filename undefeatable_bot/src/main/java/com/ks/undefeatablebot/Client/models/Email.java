package com.ks.undefeatablebot.Client.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Email {
    private final String email;

    public Email(@JsonProperty("email") String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}
