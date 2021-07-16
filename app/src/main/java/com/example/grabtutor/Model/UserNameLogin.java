package com.example.grabtutor.Model;

public class UserNameLogin {

    String email, password;

    public UserNameLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserNameLogin() {
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
