package me.vigus.twitter.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;

import me.vigus.twitter.Twitter;
import me.vigus.twitter.entities.TwitterThread;
import me.vigus.twitter.util.StreamHandler;

public class StreamSearch implements Runnable {
    private static HttpClient client = HttpClient.newHttpClient(); 
    
    private String bearerToken;
    private Twitter tweeter;

    public StreamSearch(Twitter tweeter){
        this.bearerToken = tweeter.getBearerToken();
        this.tweeter = tweeter;
    }

    @Override
    public void run(){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.twitter.com/2/tweets/search/stream?expansions=author_id,entities.mentions.username&tweet.fields=id,created_at,text,referenced_tweets,public_metrics&user.fields=id,name,username"))
                .setHeader("Authorization", "Bearer " + this.bearerToken)
                .GET()
                .build();

        HttpResponse<InputStream> x;
        try {
            x = client.send(request, BodyHandlers.ofInputStream());
        } catch (IOException | InterruptedException e1) {
            Twitter.getLogger().severe(e1.getMessage());
            return;
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(x.body()));

        Twitter.getLogger().info("Starting stream");
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue; //posible "error" (make this shit up lmao)
                }

                StreamHandler streamHandler = new StreamHandler(this.tweeter, line);
                
                CompletableFuture.supplyAsync(streamHandler)
                    .thenAccept(result -> this.tweeter.fireThreadEvent((TwitterThread)result)); //ok so first we were gonna try threading but then realised we needed a value.
            }
        } catch (IOException e) {
            Twitter.getLogger().severe(e.getMessage());
        }
    }

    /*
    public List<StreamRule> getRules() throws IOException, InterruptedException{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.twitter.com/2/tweets/search/stream/rules"))
                .setHeader("Authorization", "Bearer " + this.bearerToken)
                .GET()
                .build();

        HttpResponse<String> x = client.send(request, BodyHandlers.ofString());
        return null;
        //Do it later cant be fucked rn
        //Also give it it's own class
    }
    */


}
