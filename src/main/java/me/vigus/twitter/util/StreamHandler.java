package me.vigus.twitter.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RunnableFuture;
import java.util.function.Supplier;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.vigus.twitter.Twitter;
import me.vigus.twitter.entities.TwitterThread;
import me.vigus.twitter.json.TweetJson;

public class StreamHandler implements Supplier{

    private String response;
    private ObjectMapper mapper;
    private Twitter tweeter;

    private static final Logger LOGGER = Logger.getLogger(StreamHandler.class.getName());

    public StreamHandler(Twitter tweeter, String response){
        this.tweeter = tweeter;
        this.response = response;
        this.mapper = new ObjectMapper();
    }

    private TweetJson getTweet(String id) throws IOException, InterruptedException{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("https://api.twitter.com/2/tweets/%s?expansions=author_id,entities.mentions.username&tweet.fields=id,created_at,text,referenced_tweets,public_metrics&user.fields=id,name,username", id)))
                .setHeader("Authorization", "Bearer " + tweeter.getBearerToken())
                .GET()
                .build();

        HttpResponse<String> x = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
        return this.mapper.readValue(x.body(), TweetJson.class);
    }

    @Override
    public TwitterThread get(){
        try {
            TweetJson x = this.mapper.readValue(getResponse(), TweetJson.class);
            TwitterThread thread = x.getThread();
            String nextId = x.getNextId();

            while (nextId != null){
                TweetJson i = getTweet(nextId);
                thread.getAll().add(0, i);
                nextId = i.getNextId();
            }
            LOGGER.info("Got new thread!"); 
            return thread;
        } catch (Exception e) {
            LOGGER.severe("Error: " + e.getLocalizedMessage());
            return null;
        }
    }

    public String getResponse(){
        return response;
    }
    
}
