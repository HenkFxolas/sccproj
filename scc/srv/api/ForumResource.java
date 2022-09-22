package scc.srv.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import redis.clients.jedis.Jedis;
import scc.data.daos.Forum;
import scc.data.daos.Message;
import scc.data.dtos.MessageDTO;
import scc.srv.db.CosmosDBLayer;
import scc.srv.redis.RedisCache;
import scc.srv.search.CognitiveSearch;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/forum")
public class ForumResource {

    final CosmosDBLayer db = CosmosDBLayer.getInstance();

    final Jedis jedis = RedisCache.getCachePool().getResource();

    final boolean CACHE_FLAG = true;

    @GET
    @Path("/{search}/cognitiveSearchMessage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cognitiveSearchMessage( @PathParam("search") String search) {
        if(search == null || search.equals(""))
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("Invalid search. You need to search for something.").build());

        JsonObject resultObj = CognitiveSearch.searchMessages(search);
        List<MessageDTO> result = new LinkedList<>();

        System.out.println( "Number of results : " + resultObj.get("@odata.count").getAsInt());
        for( JsonElement el: resultObj.get("value").getAsJsonArray()) {
            JsonObject elObj = el.getAsJsonObject();
            result.add(new MessageDTO(elObj.get("id").toString(), elObj.get("sender").toString(),new Date(Long.parseLong(elObj.get("creationTime").toString())), elObj.get("subject").toString(), elObj.get("message").toString(), elObj.get("replyToId").toString(), elObj.get("forumId").toString()));
        }

        return Response.ok().entity(result).build();
    }

    @POST
    @Path("/{id}/addMessage")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMessage(@PathParam("id") String forumId, MessageDTO message) {

        Forum forum = this.getForum(forumId);

        String replyToId = message.replyToId;
        if(replyToId != null && replyToId != "")
            if(!db.getMessageById(replyToId).iterator().hasNext())
                throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("The message you are answering does not exist.").build());

        String messageId = UUID.randomUUID().toString();
        Message mess = new Message(messageId, message.sender, message.creationTime, message.subject, message.message, replyToId, forumId);

        db.putMessage(mess);

        forum.addMessage(messageId);

        db.updateForum(forum);

        return Response.ok().entity(messageId).build();
    }

    @GET
    @Path("/{id}/messages")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessages(@PathParam("id") String forumId) {
        this.getForum(forumId);
        return Response.ok().entity(this.getMessagesDTO(forumId)).build();

    }

    @DELETE
    @Path("/{forumId}/{id}")
    public Response deleteMessage(@PathParam("forumId") String forumId, @PathParam("id") String id) throws JsonProcessingException {
        Iterator<Message> itMessage = this.db.getForumMessage(id,forumId).iterator();
        if(!itMessage.hasNext())
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("Message not found.").build());

        Message message = itMessage.next();

        Iterator<Message> itMessageReply = this.db.getMessageReply(id).iterator();
        if(itMessageReply.hasNext()) {
            Message messageReply = itMessageReply.next();
            jedis.lrem("MessagesOnForum:"+forumId, 1 ,  new ObjectMapper().writeValueAsString(messageReply));
            this.db.delMessage(messageReply); //delete the reply
        }

        jedis.lrem("MessagesOnForum:"+forumId, 1 ,  new ObjectMapper().writeValueAsString(message));
        this.db.delMessage(message);

        return Response.ok().build();
    }

    private Forum getForum(String forumId){
        ObjectMapper mapper = new ObjectMapper();
        String objString = jedis.get("Forums:"+forumId); //first tries to get from cache
        Forum forum = null;

        if (CACHE_FLAG && objString != null) {
            try {
                forum = mapper.readValue(objString, Forum.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        else {
            Iterator<Forum> itForum = this.db.getForumById(forumId).iterator();
            if (!itForum.hasNext()) {
                throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("Forum not found.").build());

            } else forum = itForum.next();
        }

        return forum;
    }

    private List<MessageDTO> getMessagesDTO(String forumId) {
        boolean isCached = jedis.exists("MessagesOnForum:" + forumId);
        List<MessageDTO> messagesDTO = new LinkedList<>();
        List<Message> messages = new LinkedList<>();
        if (CACHE_FLAG && isCached) {
            ObjectMapper mapper = new ObjectMapper();
            List<String> listString = jedis.lrange("MessagesOnForum:" + forumId,0,-1); //first tries to get from cache
            listString.stream().forEach(messageString -> {
                try {
                    messages.add(mapper.readValue(messageString,Message.class));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
            messages.stream().forEach(message -> messagesDTO.add(message.toDTO()));

        } else {
            db.getForumMessages(forumId).stream().forEach(message -> messagesDTO.add(message.toDTO()));
        }

        return messagesDTO;
    }

}
