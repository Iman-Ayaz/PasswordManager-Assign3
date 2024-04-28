package com.example.passwordmanager;

public class passwordEntryModel {

    int id;
    String username;
    String password;
    String URL;


    public passwordEntryModel() {
    }

    public String getUsername() {
        return username;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
