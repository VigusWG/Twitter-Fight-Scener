package me.vigus.twitter.api;

import me.vigus.twitter.Twitter;
import me.vigus.twitter.entities.Media;
import me.vigus.twitter.entities.Tweet;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
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
        String media = String.join(", ", this.media.stream().map(Media::getId).toList());

        String json = String.format("{\"media\":{\"media_ids\":[%s]}, \"text\":%s}, \"reply\":{\"in_reply_to_tweet_id\": \"%s\"}", media, this.text, this.replyingTo.getId());

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.twitter.com/2/tweets/"))
            .setHeader("Authorization", "Bearer " + this.tweeter.getBearerToken())
            .header("Content-Type", "application/json")
            .POST(BodyPublishers.ofString(json))
            .build();        
    }
}
