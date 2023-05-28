package com.cadenza.androidnotifictions;

import java.io.Serializable;

public class User implements Serializable {
    public  String email;
    public  String token;

    public User() {
    }


    public User(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
