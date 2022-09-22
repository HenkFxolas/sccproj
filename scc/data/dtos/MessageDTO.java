package scc.data.dtos;


import java.util.Date;

public class MessageDTO {


    public String id;
    public String sender;
    public Date creationTime;
    public String subject;
    public String message;
    public String replyToId;
    public String forumId;

    public MessageDTO() {
        this.id = null;
        this.sender = null;
        this.creationTime = null;
        this.subject = null;
        this.message = null;
        this.replyToId = null;
        this.forumId = null;
    }

    public MessageDTO(String id, String sender, Date creationTime, String subject, String message, String replyToId, String forumId) {
        this.id = id;
        this.sender = sender;
        this.creationTime = creationTime;
        this.subject = subject;
        this.message = message;
        this.replyToId = replyToId;
        this.forumId = forumId;
    }

}


