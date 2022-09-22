package scc.data.daos;

import scc.data.dtos.MessageDTO;

import java.util.Date;

public class Message {

    private String id;
    private String sender;
    private Date creationTime;
    private String subject;
    private String message;
    private String replyToId;
    private String forumId;

    public Message() {
        this.id = null;
        this.sender = null;
        this.creationTime = null;
        this.subject = null;
        this.message = null;
        this.replyToId = null;
        this.forumId = null;
    }

    public Message(String id, String sender, Date creationTime, String subject, String message, String replyToId, String forumId) {
        this.id = id;
        this.sender = sender;
        this.creationTime = creationTime;
        this.subject = subject;
        this.message = message;
        this.replyToId = replyToId;
        this.forumId = forumId;
    }

    public String getReplyToId() {
        return replyToId;
    }

    public void setReplyToId(String replyToId) {
        this.replyToId = replyToId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageDTO toDTO() {
        return new MessageDTO(this.id, this.sender, this.creationTime, this.subject, this.message, this.replyToId, this.forumId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getForumId() {
        return forumId;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }
}
