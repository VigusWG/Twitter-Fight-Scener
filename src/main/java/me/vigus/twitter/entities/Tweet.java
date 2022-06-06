package me.vigus.twitter.entities;

import java.util.Date;

public interface Tweet {
    public String getId();

    public String toString();    

    public String getText();

    public TwitterThread getThread();

    public Integer getLikes();

    public Date getTimestamp();

    public User getAuthor();
}