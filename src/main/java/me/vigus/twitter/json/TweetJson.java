
package me.vigus.twitter.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import me.vigus.twitter.api.Reply;
import me.vigus.twitter.entities.TwitterThread;
import me.vigus.twitter.entities.Tweet;
import me.vigus.twitter.entities.User;

public class TweetJson implements Tweet {
    private User author;
    private String formattedText;
    private TwitterThread thread;

    @JsonProperty("data")
    public DataJson data;
    @JsonProperty("includes")
    public IncludesJson includes;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public String getNextId(){
        if (this.data.referencedTweets == null){
            return null;
        }
        return this.data.referencedTweets.get(0).id;
    }

    @Override
    public String getId() {
        return this.data.id;
    }

    @Override
    public TwitterThread getThread() {
        if (thread == null){
            thread = new TwitterThread() {
                List<Tweet> tweets = new ArrayList<>(Arrays.asList((Tweet) TweetJson.this));
                
                @Override
                public Tweet getFirst() {
                    return tweets.get(0);
                }

                @Override
                public Tweet getLast() {
                    return tweets.get(tweets.size() - 1);
                }

                @Override
                public List<Tweet> getAll() {
                    return tweets;
                }

                @Override
                public Integer getThreadLength() {
                    return tweets.size();
                }
            };
        }
        return thread;
    }

    @Override
    public Integer getLikes() {
        return this.data.publicMetrics.likeCount;
    }

    @Override
    public Date getTimestamp() {
        return this.data.createdAt;
    }

    @Override
    public User getAuthor() {
        if (author != null){
            return author;
        }

        return new User() {
            //idk if this is bad code; i hope not lol
            private UserJson author = TweetJson.this.includes.users.get(0);

            @Override
            public String getName() {
                return author.name;
            }

            @Override
            public String getUsername() {
                return author.username;
            }

            @Override
            public String getId() {
                return author.id;
            }
        };
    }

    @Override
    public String getText() {
        //fun lil error here with formatting
        if (this.formattedText == null){
            int lentocut = this.includes.users.stream()
                .skip(1)
                .mapToInt((UserJson a) -> a.username.length() + 2)
                .sum();

            this.formattedText = this.data.text.substring(lentocut);
        }


        return this.formattedText;
    }

}
