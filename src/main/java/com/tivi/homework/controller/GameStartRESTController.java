package com.tivi.homework.controller;


import com.tivi.homework.email.EmailService;
import com.tivi.homework.model.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GameStartRESTController {

    protected final static String herokuURL = "http://localhost:8080";

    protected final static String GAME_EMAIL = System.getProperty("user.dir") +
            File.separator + "src" +
            File.separator + "main" +
            File.separator + "resources" +
            File.separator + "templates" +
            File.separator + "gameEmail.html";

    protected final static String GAME_FINISHED_EMAIL = System.getProperty("user.dir") +
            File.separator + "src" +
            File.separator + "main" +
            File.separator + "resources" +
            File.separator + "templates" +
            File.separator + "gameFinishedEmail.html";

    protected static List<GameSession> gameSessionList = new ArrayList<>();

    protected  static List<User> users = new ArrayList<>();

    @PostMapping("/start")
    public String start(@RequestBody StartForm startForm) throws NoSuchAlgorithmException, InvalidKeyException, MessagingException, IOException {
        System.out.println(startForm);
        GameSession gameSession = initiateSession(startForm);
        gameSessionList.add(gameSession);

        try {
            if (gameSession.getGame().isFirstUser()){
                EmailService.sendMessage(startForm.getFirstUser(),startHTML(gameSession),gameSession);
            } else {
                EmailService.sendMessage(startForm.getSecondUser(), startHTML(gameSession), gameSession);
            }
        } catch (SendFailedException e){
            startForm.setFirstUser("");
            startForm.setSecondUser("");
            return "None";
        }
        if (gameSession.getGame().isFirstUser()){
            return "First";
        }
        return "Second";
    }

    protected static GameSession findSession(Long id){
        for (GameSession gameSession : gameSessionList){
            if (gameSession.getId().equals(id)){
                return gameSession;
            }
        }
        return null;
    }

    private GameSession initiateSession(StartForm startForm) throws InvalidKeyException, NoSuchAlgorithmException {
        User first = null;
        User second = null;
        for (User us : users) {
            if (us.getEmail().equals(startForm.getFirstUser())) {
                first = us;
            }
            if (us.getEmail().equals(startForm.getSecondUser())) {
                second = us;
            }
        }
        if (first == null) {
            first = new User(startForm.getFirstUser());
            users.add(first);
        }
        if (second == null) {
            second = new User(startForm.getSecondUser());
            users.add(second);
        }
        GameSession gameSession = new GameSession(first, second, new Game(), HmacOneTimePasswordGenerator.getGeneratedOneTimePassword());
        first.addGame(gameSession);
        second.addGame(gameSession);
        return gameSession;
    }

    //Creating email with links to moves
    private String startHTML(GameSession gameSession) throws IOException {
        File reader = new File(GAME_EMAIL);
        Document doc = Jsoup.parse(reader, "UTF-8");
        //System.out.println(doc.text());
        int i = 0;
        for (char symbol : gameSession.getGame().getField()) {
            Element a = doc.getElementById("p"+i);
            a.attr("href",herokuURL+"/"+gameSession.getId()+"/"+gameSession.getHotp()+"/"+i);
            i++;
        }
        Element user = doc.getElementById("userName");
        Element symbol = doc.getElementById("symbol");
        if (gameSession.getGame().isFirstUser()){
            user.text(gameSession.getSecondUser().getEmail());
            symbol.text("X");
        } else {
            user.text(gameSession.getFirstUser().getEmail());
            symbol.text("O");
        }
        Element gameLink = doc.getElementById("gameStatus");
        gameLink.attr("href", herokuURL+"/"+gameSession.getId());
        return String.valueOf(doc);
    }

}

