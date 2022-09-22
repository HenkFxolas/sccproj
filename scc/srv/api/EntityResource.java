package scc.srv.api;

import com.azure.cosmos.util.CosmosPagedIterable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;
import scc.data.daos.*;
import scc.data.daos.Calendar;
import scc.data.dtos.CalendarDTO;
import scc.data.dtos.EntityDTO;
import scc.data.dtos.ReservationDTO;
import scc.srv.db.CosmosDBLayer;
import scc.srv.redis.RedisCache;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;


@Path("/entity")
public class EntityResource {

    final CosmosDBLayer db = CosmosDBLayer.getInstance();
    final Jedis jedis = RedisCache.getCachePool().getResource();
    final boolean CACHE_FLAG = true;

    private static final int MAX_MEDIA_IDS = 10;

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response PostEntity(EntityDTO ent) {

        String forumId = UUID.randomUUID().toString();
        Forum forum = new Forum(forumId, new LinkedList<String>());
        db.putForum(forum);

        Entity dbEnt = new Entity(UUID.randomUUID().toString(),ent.name,ent.description,ent.mediaIds,ent.calendarId,forumId,ent.listed,ent.duration, new Date());
        db.putEntity(dbEnt);

        return Response.ok().entity(dbEnt.getId()).build();
    }

    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCalendar(@PathParam("id")String id, CalendarDTO calendar){
        Entity entity = this.getEntityDAO(id);
        String calId = entity.getCalendarId();

        if(calId != null && !calId.equals("")) //already has one calendar
            throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity("The entity " + id + " already has a calendar.").build());

        String calendarId = UUID.randomUUID().toString();
        Calendar calendarDAO = new Calendar(calendarId,calendar.name, calendar.description, new LinkedList<String>(), calendar.opening, calendar.closing);
        db.putCalendar(calendarDAO);

        entity.setCalendarId(calendarId);
        this.db.updateEntity(entity);

        return Response.ok().entity(calendarId).build();
    }

    @GET
    @Path("/getMostLiked")
    @Produces(MediaType.APPLICATION_JSON)
    public Response mostLikedEntities(){

       List<EntityDTO> mostLikedEntities = new LinkedList<>();

        ObjectMapper mapper = new ObjectMapper();
        String objString = jedis.get("serverless:cosmos:PopularEntities"); //first tries to get from cache


        if (CACHE_FLAG && objString != null) {
            try {
                List<Entity> entities = mapper.readValue(objString, new TypeReference<List<Entity>>(){});
                entities.forEach(entity -> mostLikedEntities.add(entity.toDTO()));

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        else {
            CosmosPagedIterable<Entity> entities = this.db.mostLikedEntities();
            entities.forEach(entity -> mostLikedEntities.add(entity.toDTO()));

        }

        return Response.ok().entity(mostLikedEntities).build();
    }

    @GET
    @Path("/getMostRecent")
    @Produces(MediaType.APPLICATION_JSON)
    public Response mostRecentEntities(){

        List<EntityDTO> mostRecentEntities = new LinkedList<>();

        ObjectMapper mapper = new ObjectMapper();
        String objString = jedis.get("serverless:cosmos:RecentEntities"); //first tries to get from cache

        if (CACHE_FLAG && objString != null) {
            try {
                List<Entity> entities = mapper.readValue(objString, new TypeReference<List<Entity>>(){});
                entities.forEach(entity -> mostRecentEntities.add(entity.toDTO()));

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        else {
            CosmosPagedIterable<Entity> entities = this.db.mostRecentEntities();
            entities.forEach(entity -> mostRecentEntities.add(entity.toDTO()));

        }
        return Response.ok().entity(mostRecentEntities).build();
    }

    @GET
    @Path("/{date}/{entityId}/getReservationsOfTheDay")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReservationsOfTheDay(@PathParam("date") Long date, @PathParam("entityId") String entityId) {

        Date d = new Date(date);
        Entity ent = this.getEntityDAO(entityId);
        CosmosPagedIterable<Reservation> reservations = this.db.getReservationsOfTheDay(d,ent);

        List<ReservationDTO> reservationDTOS = new LinkedList<>();
        reservations.stream().forEach(reservation -> reservationDTOS.add(reservation.toDTO()) );

        return Response.ok().entity(reservationDTOS).build();
    }

    @GET
    @Path("/{date}/{entityId}/getEmptyPeriods")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmptyPeriods(@PathParam("date") Long date, @PathParam("entityId") String id ){
        Date d = new Date(date);
        Entity ent = this.getEntityDAO(id);
        Calendar calendar = this.checkCalendar(ent.getCalendarId(),false);

        return Response.ok().entity(this.db.getEmptyPeriods(d,calendar)).build();
    }

    @PUT
    @Path("/{id}/like/{isLike}/{demanderContact}")
    public Response like(@PathParam("id") String id, @PathParam("isLike") boolean isLike,@PathParam("demanderContact") String demanderContact){

        Entity ent = this.getEntityDAO(id);
        List<String> likes = ent.getLikes();
        Calendar calendar = this.checkCalendar(ent.getCalendarId(),false);

        if (!this.db.checkDemanderReservation(calendar.getId(),demanderContact).iterator().hasNext() )
            throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity("Only users that have made reservations can like or dislike the entity.").build());

        if(isLike) {
            if(likes.contains(demanderContact))
                throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity("This user has already liked the entity.").build());
            likes.add(demanderContact);
        }
        else {
            if(!likes.contains(demanderContact))
                throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity("This user has already disliked the entity.").build());
            likes.remove(demanderContact);
        }
        ent.setLikes(likes);
        ent.setNumberOfLikes(likes.size());
        this.db.updateEntity(ent);

        return Response.ok().build();
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response GetEntity(@PathParam("id") String id) {
        Entity ent = this.getEntityDAO(id);
        return Response.ok().entity(ent.toDTO()).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEntity(EntityDTO ent, @PathParam("id") String id) {
        Iterator<Entity> itEnt = db.getEntityById(id).iterator();

        if(!itEnt.hasNext())
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("The entity " + id + " does not exists.").build());

        Entity entity = itEnt.next();

        entity.setListed(ent.listed);

        if(ent.name != null)
            entity.setName(ent.name);
        if(ent.description != null)
            entity.setDescription(ent.description);
        if(ent.duration != null)
            entity.setDuration(ent.duration);

        db.updateEntity(entity);

        return Response.ok().build();

    }

    @DELETE
    @Path("/{id}")
    public Response deleteEntity(@PathParam("id") String id) {
        Entity entity = this.getEntityDAO(id);
        Calendar calendar = this.checkCalendar(entity.getCalendarId(),true);
        if(calendar != null) {
            db.delCalendar(calendar);
            //cache
            jedis.del("Calendars:"+entity.getCalendarId());
        }

        db.delEntity(entity);
        //cache
        jedis.del("Entities:"+entity.getId());

        return Response.ok().build();
    }

    @PUT
    @Path("/{id}/{mediaId}/addMediaId")
    public Response addMediaId(@PathParam("id") String id, @PathParam("mediaId") String mediaId){
        Entity entity = this.getEntityDAO(id);
        List<String> mediaIds = entity.getMediaIds();
        if(mediaIds.size() ==  MAX_MEDIA_IDS)
            throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity("The entity " + id + " has already achieved the max number of photos.").build());
        mediaIds.add(mediaId);
        entity.setMediaIds(mediaIds);
        this.db.updateEntity(entity);

        return Response.ok().entity(mediaId).build();
    }

    private Calendar checkCalendar(String calendarId, boolean isDelete){
        ObjectMapper mapper = new ObjectMapper();
        String objString = jedis.get("Calendars:"+calendarId); //first tries to get from cache
        Calendar resp = null;

        if (CACHE_FLAG && objString != null) {
            try {
                resp = mapper.readValue(objString, Calendar.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        else {
            Iterator<Calendar> itCalendar = this.db.getCalendarById(calendarId).iterator();
            if (!itCalendar.hasNext()) {
                if (!isDelete)
                    throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("The calendar with id: " + calendarId + " does not exists.").build());

            } else resp = itCalendar.next();
        }

        return resp;
    }





    private Entity getEntityDAO(String id){

        ObjectMapper mapper = new ObjectMapper();
        String objString = jedis.get("Entities:"+id); //first tries to get from cache

        Entity ent = null;


        if (CACHE_FLAG && objString != null) {
            try {
                ent = mapper.readValue(objString, Entity.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        else {
            Iterator<Entity> itEnt = db.getEntityById(id).iterator();

            if(!itEnt.hasNext())
                throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("The entity " + id + " does not exists.").build());

            ent = itEnt.next();

        }

        return ent;
    }


}
