package com.entigrity.model.topicsofinterestn;

import com.google.gson.annotations.SerializedName;


public class TagsItem {

    @SerializedName("id")
    private int id;

    @SerializedName("tag")
    private String tag;

    @SerializedName("topic_of_interest_id")
    private int topicOfInterestId;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTopicOfInterestId(int topicOfInterestId) {
        this.topicOfInterestId = topicOfInterestId;
    }

    public int getTopicOfInterestId() {
        return topicOfInterestId;
    }

    @Override
    public String toString() {
        return
                "TagsItem{" +
                        "id = '" + id + '\'' +
                        ",tag = '" + tag + '\'' +
                        ",topic_of_interest_id = '" + topicOfInterestId + '\'' +
                        "}";
    }
}