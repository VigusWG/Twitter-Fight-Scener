package me.vigus;

import java.io.IOException;
import java.util.logging.Logger;

import me.vigus.twitter.Twitter;
import me.vigus.twitter.TwitterEvents;
import me.vigus.twitter.entities.TwitterThread;
import me.vigus.twitter.entities.Tweet;

public class App implements TwitterEvents
{
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    private static String bearerToken = "AAAAAAAAAAAAAAAAAAAAAHSscgEAAAAAh3%2BytHnZPoWllhbW9ppdaGwRtwE%3DegvK9miICpV1JTw86K0spWRSrLedGeYAFJ3pnkLJ0wYRN8i2P5";
    
    public static void main( String[] args ) throws IOException, InterruptedException
    {
        LOGGER.info( "Starting." ); //woah why the fuck you not using a logger dip-shit - lemme complain at you for fucking ever.

        Twitter twit = new Twitter(bearerToken);
        twit.addListener(new App());

        twit.startStreamSearch(); //blocks for the rest ... oh no it fucken doesnt lets goooooo
        
    }

    @Override
    public void onNewThread(TwitterThread thread) {
        for (Tweet i: thread.getAll()){
            System.out.println(i.getText());
        }
    }

}