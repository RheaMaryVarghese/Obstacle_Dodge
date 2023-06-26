package com.example.game;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ApiResponse {
    private String tip;
    private List<Character> characters;
    public String getTip() {
        return tip;
    }

    public List<Character> getCharacters() {
        return characters;
    }
   /* private List<Score> scores;

    public List<Score> getScores() {
        return scores;
    }*/

    public void setTip(String tip) {
        this.tip = tip;
    }
}

