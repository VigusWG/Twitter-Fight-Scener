package me.vigus.twitter.api;

import me.vigus.twitter.Twitter;
import me.vigus.twitter.entities.Media;
import me.vigus.twitter.entities.Tweet;

import java.util.ArrayList;

public class Reply implements Runnable{
    private Twitter tweeter;
    private Tweet replyingTo;
    private String text;
    private ArrayList<Media> media;


    public Reply(Twitter tweeter, Tweet replyingTo, String text, ArrayList<Media> media){
        this.tweeter = tweeter;
        this.replyingTo = replyingTo;
        this.text = text;
        this.media = media; 
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        
    }
}
