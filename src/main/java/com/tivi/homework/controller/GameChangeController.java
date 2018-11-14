package com.tivi.homework.controller;

import com.tivi.homework.email.EmailService;
import com.tivi.homework.model.Game;
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
import static com.tivi.homework.controller.GameStartRESTController.GAME_FINISHED_EMAIL;
import static com.tivi.homework.controller.GameStartRESTController.HEROKU_URL;

@Controller
public class GameChangeController {

    @GetMapping("/{session}/{hotp}/{pos}")
    public String checkChange(@PathVariable Long session,
                              @PathVariable int hotp,
                              @PathVariable int pos,
                              Model model) throws IOException, MessagingException, InvalidKeyException, NoSuchAlgorithmException {
        GameSession gameSession = GameStartRESTController.findSession(session);
        if (gameSession == null){
            model.addAttribute("response", "Wrong session!");
            return "emailResponse";
        }
        if (gameSession.getHotp() != hotp){
            model.addAttribute("response", "It's not yout turn!");
            return "emailResponse";
        }
        if (pos > 8 || pos < 0){
            model.addAttribute("response", "Wrong move!");
            return "emailResponse";
        }
        Game game = gameSession.getGame();
        game.changePosition(pos);
        gameSession.setHotp(HmacOneTimePasswordGenerator.getGeneratedOneTimePassword());
        if (game.isFinished()){

            EmailService.sendMessage(gameSession.getFirstUser().getEmail(), endHTML(gameSession),gameSession);
            EmailService.sendMessage(gameSession.getSecondUser().getEmail(), endHTML(gameSession), gameSession);
            model.addAttribute("response","Game is finished!");
            return "emailResponse";
        }
        if (!game.isFirstUser()){
            EmailService.sendMessage(gameSession.getSecondUser().getEmail(), changeHTML(gameSession), gameSession);
        } else {
            EmailService.sendMessage(gameSession.getFirstUser().getEmail(), changeHTML(gameSession), gameSession);
        }
        model.addAttribute("response", "Move is registered!");
        return "emailResponse";
    }

    private String changeHTML(GameSession gameSession) throws IOException {
        Game game = gameSession.getGame();
        File reader = new File(GAME_EMAIL);
        Document doc = Jsoup.parse(reader, "UTF-8");
        int i = 0;
        for (char symbol : game.getField()){
            Element a =doc.getElementById("p"+i);
            if (symbol == ' '){
                a.attr("href", HEROKU_URL +"/"+ gameSession.getId()+ "/"+ gameSession.getHotp()+ "/"+i);
            } else {
                a.text(String.valueOf(symbol));
            }
            i++;
        }
        Element symbol = doc.getElementById("symbol");
        if (game.isFirstUser()){
            symbol.text("X");
        } else {
            symbol.text("O");
        }

        Element gameLink = doc.getElementById("gameStatus");
        gameLink.attr("href", HEROKU_URL +"/"+gameSession.getId());
        return String.valueOf(doc);
    }

    private String endHTML(GameSession gameSession) throws IOException {
        Game game = gameSession.getGame();
        File reader = new File(GAME_FINISHED_EMAIL);
        Document doc = Jsoup.parse(reader,"UTF-8");
        int i = 0;
        for (char symbol : game.getField()){
            Element h =doc.getElementById("p"+i);
            h.text(String.valueOf(symbol));
            i++;
        }
        Element p = doc.getElementById("playerWon");
        if (game.isFirstUser()){
            p.text(gameSession.getSecondUser().getEmail());
        } else {
            p.text(gameSession.getFirstUser().getEmail());
        }
        return String.valueOf(doc);
    }
}
