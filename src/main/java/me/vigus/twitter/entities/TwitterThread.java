package me.vigus.twitter.entities;

import java.util.List;

/**
 * @author Vigus Widdicombe Gasson
 */
public interface TwitterThread {
    /**
     * Get first tweet.
     * <p>
     * The first tweet in the thread.
     * <p>
     *
     * @return Tweet the tweet.
     */
    public Tweet getFirst();

    public Tweet getLast();

    public List<Tweet> getAll();

    public Integer getThreadLength();
}