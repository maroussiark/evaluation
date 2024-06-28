package com.project.config;

public class User {
    String id;
    String username;
    String profil;

    public User() {
    }

    public User(String id, String username, String profil) {
        this.id = id;
        this.username = username;
        this.profil = profil;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfil() {
        return this.profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
