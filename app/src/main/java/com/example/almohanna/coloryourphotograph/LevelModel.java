package com.example.almohanna.coloryourphotograph;

/**
 * Created by Reem on 28-Dec-17.
 */

public class LevelModel {

    String level;
    String levelId;

    public LevelModel() {

    }
    public LevelModel(String level_id, String level) {
        this.level = level;
        this.levelId = level_id;
    }


    public String getlevelId() {
        return levelId;
    }

    public void setlevelId(String Id) {
        this.levelId = Id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String l) {
        this.level = l;
    }
}
