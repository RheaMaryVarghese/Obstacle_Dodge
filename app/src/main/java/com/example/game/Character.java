package com.example.game;

public class Character {
    private String name,description,type,imageUrl;

    public Character(String name, String description, String type, String imageUrl) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

  /*  public int getScore() {
        return score;
    }*/
}
