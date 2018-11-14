package com.tivi.homework.model;

public class StartForm {

    private String firstUser;

    private String secondUser;

    public StartForm() {
    }

    public StartForm(String firstUser, String secondUser) {
        this.firstUser = firstUser;
        this.secondUser = secondUser;
    }

    public String getFirstUser() {
        return firstUser;
    }

    public void setFirstUser(String firstUser) {
        this.firstUser = firstUser;
    }

    public String getSecondUser() {
        return secondUser;
    }

    public void setSecondUser(String secondUser) {
        this.secondUser = secondUser;
    }

    @Override
    public String toString() {
        return "StartForm{" +
                "firstUser='" + firstUser + '\'' +
                ", secondUser='" + secondUser + '\'' +
                '}';
    }
}
