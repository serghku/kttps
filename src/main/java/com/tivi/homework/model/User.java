package com.tivi.homework.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String email;

    private List<GameSession> sessions;

    public User(String email) {
        this.email = email;
        this.sessions = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", sessions=" + sessions +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<GameSession> getSessions() {
        return sessions;
    }

    public void setSessions(List<GameSession> sessions) {
        this.sessions = sessions;
    }

    public void addGame(GameSession gs){
        sessions.add(gs);
    }


}
