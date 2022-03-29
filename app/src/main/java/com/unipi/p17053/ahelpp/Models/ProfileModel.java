package com.unipi.p17053.ahelpp.Models;

public class ProfileModel {

    private String name;
    private String email;
    private int bookmarksCount;
    private int wrongsCount;

    public ProfileModel(String name, String email,int bookmarksCount,int wrongsCount) {
        this.name = name;
        this.email = email;
        this.bookmarksCount = bookmarksCount;
        this.wrongsCount = wrongsCount;

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

    public int getBookmarksCount() {
        return bookmarksCount;
    }

    public void setBookmarksCount(int bookmarksCount) {
        this.bookmarksCount = bookmarksCount;
    }

    public int getWrongsCount() {
        return wrongsCount;
    }

    public void setWrongsCount(int wrongsCount) {
        this.wrongsCount = wrongsCount;
    }

}
