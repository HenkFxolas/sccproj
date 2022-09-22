package scc.data;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Entity {

    private String id;
    private String name, description;
    private String calendarId;
    private String forumId;
    private boolean listed;
    private Long duration;
    private List<String> mediaIds;
    private List<String> likes;
    private int numberOfLikes;
    private Date creationTime;

    public Entity() {
        id = null;
        name = null;
        description = null;
        mediaIds = null;
        calendarId = null;
        forumId = null;
        listed = false;
        duration = null;
        likes = null;
        numberOfLikes = -1;
        creationTime = null;
    }


    public Entity(String id, String name, String description, List<String>mediaIds, String calendarId, String forumId, boolean listed, Long duration, Date creationTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.mediaIds = mediaIds;
        this.calendarId = calendarId;
        this.forumId = forumId;
        this.listed = listed;
        this.duration = duration;
        this.likes = new LinkedList<>();
        this.numberOfLikes = 0;
        this.creationTime = creationTime;
    }

    public String getForumId() {
        return forumId;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getMediaIds() {
        return mediaIds;
    }

    public void setMediaIds(List<String> mediaIds) {
        this.mediaIds = mediaIds;
    }

    public boolean isListed() {
        return listed;
    }

    public void setListed(boolean listed) {
        this.listed = listed;
    }

    @Override
    public String toString() {
        return "Entity [id=" + id + ", name=" + name + ", description=" + description + ", mediaIds="
                + mediaIds.toString() + ", calendarId=" + calendarId + ", listed=" + listed +", likes="+likes.toArray().toString() + ", numberOfLikes="+numberOfLikes+", creationTime="+creationTime+"]";
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public int getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
}
