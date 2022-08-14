package me.vigus.twitter.api;

import java.util.ArrayList;

import me.vigus.twitter.Twitter;
import me.vigus.twitter.entities.StreamRule;

public class StreamRules {

    private String bearerToken;
    private Twitter tweeter;

    public StreamRules(Twitter tweeter){
        this.bearerToken = tweeter.getBearerToken();
        this.tweeter = tweeter;
    }

    public void clearRules(){
        ;
    }

    public ArrayList<StreamRule> getRules(){
        return null;
    }

    public void addRules(){
        ;
    }
}
