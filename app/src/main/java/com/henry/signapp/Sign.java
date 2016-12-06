package com.henry.signapp;

/**
 * Created by tom on 12/2/16.
 */

public class Sign {
    private String url, title;
    private int followersCount,mastersCount;


    //constructors
    public Sign(){

    }
    public Sign(String gifURL, String title){
        url = gifURL;
        this.title = title;
        followersCount=0;
        mastersCount=0;
    }

    //getters & setter methods for SignGif
    public String getUrl(){
        return url;
    }

    public String getTitle(){
        return title;
    }

    public int getFollowers(){
        return followersCount;
    }
    public int getMastersCount(){
        return mastersCount;
    }


}
