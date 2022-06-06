package me.vigus.twitter;

import me.vigus.twitter.entities.TwitterThread;

public interface TwitterEvents {
    void onNewThread(TwitterThread thread);
}
