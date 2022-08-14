package me.vigus.twitter.builders;

import java.util.ArrayList;

import me.vigus.twitter.api.Reply;
import me.vigus.twitter.entities.Media;
import me.vigus.twitter.entities.Tweet;

public class ReplyBuilder {
    private String text;
    private Tweet reply;
    private ArrayList<Media> media = new ArrayList<>();

    public ReplyBuilder(Tweet reply){
        this.reply = reply;
    }

    Reply make(){
        
        return null;
    }

    

    
}
