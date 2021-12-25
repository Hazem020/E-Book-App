package com.example.yourbook;

public class Book {
    /**
     * This Class to store information of Book to connect with database
     */

    // Class attributes
    String name, cover, pdf,audio;

    // empty constructor for firebase
    public Book() {
    }

    // return pdf of book from firebase
    public String getPdf() {
        return pdf;
    }
    // return audio from firebase
    public String getAudio() {
        return audio;
    }
    // return name from firebase
    public String getName() { return name; }
    // return cover frome firebase
    public String getCover() { return cover; }
}

