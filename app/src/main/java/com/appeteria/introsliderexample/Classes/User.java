package com.appeteria.introsliderexample.Classes;

/**
 * Created by BDDL-102 on 6/23/2018.
 */

public class User {
    private String email;
    private String password;
    private int id;

    public User(int id, String email, String password) {

        this.id = id;
        this.email = email;
        this.password = password;
    }

    public User( String email, String password) {

        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
