package com.project.altysh.firebaseloginandsaving.dto;

/**
 * Created by Altysh on 3/7/2018.
 */

public class UserProfile {
    public String name;
    public String email;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public UserProfile() {
    }

    public UserProfile(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "USername " + name + " email " + email;
    }
}
