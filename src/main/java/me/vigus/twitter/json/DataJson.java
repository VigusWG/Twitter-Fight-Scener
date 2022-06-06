
package me.vigus.twitter.json;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DataJson{

    @JsonProperty("author_id")
    public String authorId;

    @JsonProperty("text")
    public String text;

    @JsonProperty("entities")
    public EntitiesJson entities;

    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    public Date createdAt;
    @JsonProperty("id")
    public String id;

    @JsonProperty("public_metrics")
    public PublicMetricsJson publicMetrics;

    @JsonProperty("referenced_tweets")
    public List<ReferencedTweetJson> referencedTweets = null;

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

}
