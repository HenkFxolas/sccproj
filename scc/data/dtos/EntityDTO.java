package scc.data.dtos;


import java.util.List;

public class EntityDTO {


    public String id,name, description;
    public List<String> mediaIds;
    public String calendarId;
    public boolean listed;
    public Long duration;
    public String forumId;
    public int numberOfLikes;
    public EntityDTO(String id, String name, String description, List<String> mediaIds, String calendarId, boolean listed, Long duration, String forumId, int numberOfLikes){
        this.id = id;
        this.name = name;
        this.description = description;
        this.calendarId = calendarId;
        this.mediaIds = mediaIds;
        this.listed = listed;
        this.duration = duration;
        this.forumId = forumId;
        this.numberOfLikes = numberOfLikes;
    }

    public EntityDTO() {
        this.id = null;
        this.name = null;
        this.description = null;
        this.calendarId = null;
        this.mediaIds = null;
        this.listed = false;
        this.duration = null;
        this.forumId = null;
        this.numberOfLikes = -1;
    }

    public boolean checkFields() {
        return (id != null && name != null && description != null && mediaIds != null && calendarId != null
                && duration != null && forumId != null);
    }

}
