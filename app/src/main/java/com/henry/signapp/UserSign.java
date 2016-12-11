package com.henry.signapp;

/**
 * Created by Henry on 12/8/2016.
 */
public class UserSign {
    private String title, url, category;
    boolean mastered;


    public UserSign(){

    }
    public UserSign(String title, String url, String category){
        this.category = category;
        this.title = title;
        this.url = url;
    }

    public String getUrl(){
        return this.url;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }
    public void setMastered(boolean value){
        this.mastered = value;
    }
    public boolean getMastered(){
        return mastered;
    }
    public void setCategory(String cat){
        this.category = cat;
    }
    public String getCategory(){
        return category;
    }


}
