package com.tivi.homework.controller;

import com.tivi.homework.model.GameSession;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.io.IOException;

@Controller
public class StatusController {

    protected final static String GAME_STATUS = System.getProperty("user.dir") +
            File.separator + "src" +
            File.separator + "main" +
            File.separator + "resources" +
            File.separator + "templates" +
            File.separator + "gameStatus.html";

    @GetMapping("/{id}")
    public String showStatus(@PathVariable Long id, Model model) throws IOException {
        GameSession gameSession = GameStartRESTController.findSession(id);

        if (gameSession == null){
            model.addAttribute("response", "Wrong session ID!");
            return "gameStatus";
        }
        model.addAttribute("response", "Game status");
        model.addAttribute("sessionId","Session:"+gameSession.getId());
        model.addAttribute("row1", gameSession.getGame().toStringHTMLinRows(1));
        model.addAttribute("row2", gameSession.getGame().toStringHTMLinRows(2));
        model.addAttribute("row3", gameSession.getGame().toStringHTMLinRows(3));
        return "gameStatus";
    }
}
