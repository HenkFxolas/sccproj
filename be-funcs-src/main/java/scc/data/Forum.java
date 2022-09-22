package scc.data;

import java.util.List;

public class Forum {

    public String id;

    public List<String> messages;

    public Forum() {
        this.id = null;
        this.messages = null;
    }

    public Forum(String id, List<String> messages) {
        this.id = id;
        this.messages = messages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void addMessage(String messageId) {
        messages.add(messageId);
    }


}
