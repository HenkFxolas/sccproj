package scc.serverless;

import com.google.gson.*;
import com.microsoft.azure.functions.annotation.*;

import redis.clients.jedis.Jedis;
import scc.data.*;
import scc.srv.RedisCache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;
import java.util.Date;

public class CosmosDBFunction {

    //Functions that update cache on writes, not including delete

    @FunctionName("updateCacheEntityOnPuts")
    public void cosmosDbProcessorEntity(
        @CosmosDBTrigger(name = "cacheEntityUpdate",
            databaseName = "sccdb3phr",
            collectionName = "Entities",
            createLeaseCollectionIfNotExists = true,
            connectionStringSetting = "AzureCosmosDBConnection") String[] items) throws JsonProcessingException {

		try (Jedis jedis = RedisCache.getCache().getJedisPool().getResource()) {
		    //String ent = items[0]; //expecting the collection to change in only one element for each trigger
            //essential for data deserialization
            final Gson builder = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                        public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) {
                            return new Date(jsonElement.getAsJsonPrimitive().getAsLong());
                        }
                    })
                    .create();
            //expecting the collection to potentially change in many elements for each trigger
            for (String ent: items) {
                Entity eDAO = builder.fromJson(ent, Entity.class);
                jedis.set("Entities:" + eDAO.getId(), new ObjectMapper().writeValueAsString(eDAO));
            }


		}
    }

    @FunctionName("updateCacheCalendarOnPuts")
    public void cosmosDbProcessorCalendar(
            @CosmosDBTrigger(name = "cacheCalendarUpdate",
                    databaseName = "sccdb3phr",
                    collectionName = "Calendars",
                    createLeaseCollectionIfNotExists = true,
                    connectionStringSetting = "AzureCosmosDBConnection") String[] items) throws JsonProcessingException {

        try (Jedis jedis = RedisCache.getCache().getJedisPool().getResource()) {
            //String cal = items[0]; //expecting the collection to change in only one element for each trigger
            //expecting the collection to potentially change in many elements for each trigger
            for (String cal: items) {
                Calendar cDAO = new Gson().fromJson(cal, Calendar.class);
                jedis.set("Calendars:" + cDAO.getId(), new ObjectMapper().writeValueAsString(cDAO));
            }


        }
    }

    @FunctionName("updateCacheForumOnPuts")
    public void cosmosDbProcessorForum(
            @CosmosDBTrigger(name = "cacheForumUpdate",
                    databaseName = "sccdb3phr",
                    collectionName = "Forums",
                    createLeaseCollectionIfNotExists = true,
                    connectionStringSetting = "AzureCosmosDBConnection") String[] items) throws JsonProcessingException {

        try (Jedis jedis = RedisCache.getCache().getJedisPool().getResource()) {
            //String forum = items[0]; //expecting the collection to change in only one element for each trigger
            //expecting the collection to potentially change in many elements for each trigger
            for (String forum: items) {
                Forum fDAO = new Gson().fromJson(forum, Forum.class);
                jedis.set("Forums:" + fDAO.getId(), new ObjectMapper().writeValueAsString(fDAO));
            }

        }
    }

    @FunctionName("updateCacheReservationOnPuts")
    public void cosmosDbProcessorReservation(
            @CosmosDBTrigger(name = "cacheReservationUpdate",
                    databaseName = "sccdb3phr",
                    collectionName = "Reservations",
                    createLeaseCollectionIfNotExists = true,
                    connectionStringSetting = "AzureCosmosDBConnection") String[] items) throws JsonProcessingException {

        try (Jedis jedis = RedisCache.getCache().getJedisPool().getResource()) {
           // String res = items[0]; //expecting the collection to change in only one element for each trigger
            //essential for data deserialization
            final Gson builder = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                        public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) {
                            return new Date(jsonElement.getAsJsonPrimitive().getAsLong());
                        }
                    })
                    .create();
            //expecting the collection to potentially change in many elements for each trigger
            for (String res: items) {
                Reservation rDAO = builder.fromJson(res, Reservation.class);
                jedis.set("Reservations:" + rDAO.getId(), new ObjectMapper().writeValueAsString(rDAO));
            }

        }
    }

    @FunctionName("updateCacheMessageOnPuts")
    public void cosmosDbProcessorMessage(
            @CosmosDBTrigger(name = "cacheMessageUpdate",
                    databaseName = "sccdb3phr",
                    collectionName = "Messages",
                    createLeaseCollectionIfNotExists = true,
                    connectionStringSetting = "AzureCosmosDBConnection") String[] items) throws JsonProcessingException {

        try (Jedis jedis = RedisCache.getCache().getJedisPool().getResource()) {
           // String mess = items[0]; //expecting the collection to change in only one element for each trigger
            //essential for data deserialization
            final Gson builder = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                        public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) {
                            return new Date(jsonElement.getAsJsonPrimitive().getAsLong());
                        }
                    })
                    .create();

            //expecting the collection to potentially change in many elements for each trigger
            for (String mess: items) {
                Message mDAO = builder.fromJson(mess, Message.class);
                jedis.rpush("MessagesOnForum:" + mDAO.getForumId(), new ObjectMapper().writeValueAsString(mDAO));
            }

        }
    }





}
