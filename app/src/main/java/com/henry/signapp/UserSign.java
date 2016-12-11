package com.henry.signapp;

/**
 * Created by tom on 12/8/16.
 */

public class UserSign {
    String category, url, title;
    boolean mastered;

    //constructors
    public UserSign(){

    }
    public UserSign(String gifCategory, String gifURL, String gifTitle){
        category = gifCategory;
        url = gifURL;
        title = gifTitle;
        mastered = false;
    }

    //getters & setters
    public String getCategory(){
        return category;
    }
    public String getUrl(){
        return url;
    }
    public String getTitle(){
        return title;
    }
    public boolean isMastered(){
        return mastered;
    }
    public void setMastered(boolean gifMastered){
        mastered = gifMastered;
    }

    //toString
    public String toString(){
        return "category: " + getCategory() + ", URL: " + getUrl() + ", title: " + getTitle() +
                ", mastered: " + isMastered();
    }
}