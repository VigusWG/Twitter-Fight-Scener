package me.vigus.twitter;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import me.vigus.twitter.api.StreamSearch;
import me.vigus.twitter.entities.TwitterThread;

public class Twitter {
    private final static Logger LOGGER = Logger.getLogger(Twitter.class.getName());
    private List<TwitterEvents> listeners = new ArrayList<TwitterEvents>();

    private String bearerToken;

    public Twitter(String bearerToken){
        this.bearerToken = bearerToken;
    }

    public static Logger getLogger(){
        return LOGGER;
    }

    public String getBearerToken(){
        return bearerToken;
    }

    public void fireThreadEvent(TwitterThread thread) {
        for (TwitterEvents event : listeners){
            event.onNewThread(thread);
        }
    }

    public void addListener(TwitterEvents toAdd) {
        listeners.add(toAdd);
    }

    public void startStreamSearch() {
        StreamSearch search = new StreamSearch(this);
        Thread thread = new Thread(search);
        thread.start();
    }


}