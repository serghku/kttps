package com.tivi.homework.model;

import java.util.Random;

public class GameSession {

    private Long id;

    private User firstUser;

    private User secondUser;

    private Game game;

    private int hotp;


    public GameSession(User firstUser, User secondUser, Game game, int hotp) {
        this.id = randomLong();
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.game = game;
        this.hotp = hotp;
    }



    public void setId(Long id) {
        this.id = id;
    }

    public int getHotp() {
        return hotp;
    }

    public void setHotp(int hotp) {
        this.hotp = hotp;
    }

    private Long randomLong(){
        return Math.abs(new Random().nextLong());
    }

    public User getFirstUser() {
        return firstUser;
    }

    public Long getId() {
        return id;
    }

    public void setFirstUser(User firstUser) {
        this.firstUser = firstUser;
    }

    public User getSecondUser() {
        return secondUser;
    }

    public void setSecondUser(User secondUser) {
        this.secondUser = secondUser;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
