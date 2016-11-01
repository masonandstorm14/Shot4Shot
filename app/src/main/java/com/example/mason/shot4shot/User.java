package com.example.mason.shot4shot;

/**
 * Created by mason on 9/19/2016.
 */
public class User {
    protected String username;
    protected String email;
    protected Boolean newAccount;

    public User(){

    }

    public User(String username, String email, Boolean newAccount){
        this.username = username;
        this.email = email;
        this.newAccount = newAccount;
    }
}
