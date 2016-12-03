package com.henry.signapp;

/**
 * Created by Henry on 12/3/2016.
 */
public class Gif {
    public String idd;
    public boolean mastered;

    public Gif(String idd, boolean mastered){
        this.idd = idd;
        this.mastered = mastered;

    }
    public Gif(){
        this.idd = "all";
        this.mastered = false;

    }
}
