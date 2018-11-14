package com.tivi.homework.controller;

import com.tivi.homework.email.EmailService;
import com.tivi.homework.model.GameSession;
import com.tivi.homework.model.HmacOneTimePasswordGenerator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static com.tivi.homework.controller.GameStartRESTController.GAME_EMAIL;
import static com.tivi.homework.controller.GameStartRESTController.HEROKU_URL;

@Controller
public class InvitationController {


    @GetMapping("/{session}/{hotp}")
    public String invitationCheck(@PathVariable Long session,
                                  @PathVariable int hotp,
                                  Model model) throws InvalidKeyException, NoSuchAlgorithmException, MessagingException, IOException {
        GameSession gameSession = GameStartRESTController.findSession(session);
        if (gameSession == null || gameSession.getHotp() != hotp){
            model.addAttribute("response", "Forbidden");
            return "emailResponse";
        }
        gameSession.setHotp(HmacOneTimePasswordGenerator.getGeneratedOneTimePassword());
        if (gameSession.getGame().isFirstUser()){
            EmailService.sendMessage(gameSession.getFirstUser().getEmail(),startHTML(gameSession),gameSession);
        } else {
            EmailService.sendMessage(gameSession.getSecondUser().getEmail(), startHTML(gameSession), gameSession);
        }
        if (gameSession.getGame().isFirstUser()){
            model.addAttribute("response","First user will start");
            return "emailResponse";
        }
        model.addAttribute("response", "Second user will start");
        return "emailResponse";

    }
    //Creating email with links to moves
    private String startHTML(GameSession gameSession) throws IOException {
        File reader = new File(GAME_EMAIL);
        Document doc = Jsoup.parse(reader, "UTF-8");
        int i = 0;
        for (char symbol : gameSession.getGame().getField()) {
            Element a = doc.getElementById("p"+i);
            a.attr("href", HEROKU_URL +"/"+gameSession.getId()+"/"+gameSession.getHotp()+"/"+i);
            i++;
        }
        Element symbol = doc.getElementById("symbol");
        if (gameSession.getGame().isFirstUser()){
            symbol.text("X");
        } else {
            symbol.text("O");
        }
        Element gameLink = doc.getElementById("gameStatus");
        gameLink.attr("href", HEROKU_URL +"/"+gameSession.getId());
        return String.valueOf(doc);
    }
}
