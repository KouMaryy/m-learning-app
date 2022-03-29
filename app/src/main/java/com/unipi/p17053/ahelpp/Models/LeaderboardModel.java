package com.unipi.p17053.ahelpp.Models;

public class LeaderboardModel {

    String name;
    private int score;
    private int rank;

    public LeaderboardModel(String name, int score, int level) {
        this.score = score;
        this.rank = level;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
