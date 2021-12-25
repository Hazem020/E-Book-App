package com.example.yourbook;

public class User {
    /**
     * This Class to store information of user to connect with database
     */

    // Class attributes
    String FullName;
    String Username;
    String Email;

    //empty constructor for firebase
    public User() {
    }
    // set name of user
    public void setFullName(String fullName) {
        FullName = fullName;
    }
    // set username of user
    public void setUsername(String username) { Username = username; }
    // set email of user
    public void setEmail(String email) {
        Email = email;
    }
    // return name of user
    public String getFullName() {
        return FullName;
    }
    // return username
    public String getUsername() {
        return Username;
    }
    // return email
    public String getEmail() {
        return Email;
    }
}
