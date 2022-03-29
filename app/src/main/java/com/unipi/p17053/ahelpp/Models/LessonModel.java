package com.unipi.p17053.ahelpp.Models;

public class LessonModel {

    private String docID;
    private  String lessonName;
    private int numOfTests;
    String  url;

    public LessonModel(String docID, String lessonName,int numOfTests,String url) {
        this.docID = docID;
        this.lessonName = lessonName;
        this.numOfTests = numOfTests;
        this.url = url;

    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public int getNumOfTests() {
        return numOfTests;
    }

    public void setNumOfTests(int numOfTests) {
        this.numOfTests = numOfTests;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
