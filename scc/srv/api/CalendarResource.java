package scc.srv.api;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;
import scc.data.daos.Calendar;
import scc.data.daos.Reservation;
import scc.data.dtos.ReservationDTO;
import scc.srv.db.CosmosDBLayer;
import scc.srv.redis.RedisCache;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Path("/calendar")
public class CalendarResource {

    final CosmosDBLayer db = CosmosDBLayer.getInstance();
    final Jedis jedis = RedisCache.getCachePool().getResource();
    final boolean CACHE_FLAG = true;

    @POST
    @Path("/{calendarId}/addReservation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReservation(@PathParam("calendarId") String calendarId, ReservationDTO reservation) {
        Calendar calendar = this.getCalendarDAO(calendarId);
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(reservation.reservationDate);
        c.set(c.SECOND, 0);
        c.set(c.MINUTE, 0);
        c.set(c.MILLISECOND, 0);
        Date reservationDate = c.getTime();
        Date end = null;
        if(calendar.getClosing() == 0)
            end = this.setDate(c,23,59,59,0);
        else
            end = this.setDate(c,calendar.getClosing(),0,0,0);

        Date start = this.setDate(c,calendar.getOpening(),0,0,0);

        if(reservationDate.getTime() >= end.getTime() || reservationDate.getTime()<start.getTime())
            throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity("Establishment closed at the time pretended.").build());

        if(db.isReservationBooked(calendarId,reservationDate).iterator().hasNext())
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT).entity("Reservation already exists.").build());

        String reservationId = UUID.randomUUID().toString();
        Reservation resDAO = new Reservation(reservationId, calendarId, reservation.demander,reservation.establishment, reservationDate, reservation.demanderContact);
        db.putReservation(resDAO);
        List<String> calRes = calendar.getReservationsIds();
        calRes.add(reservationId);
        calendar.setReservationsIds(calRes);
        db.updateCalendar(calendar);


        return Response.ok().entity(reservationId).build();
    }

    public static Date setDate(java.util.Calendar c, int hours,int minutes,int seconds, int milli) {
        c.set(c.HOUR_OF_DAY,hours);
        c.set(c.SECOND, seconds);
        c.set(c.MINUTE, minutes);
        c.set(c.MILLISECOND, milli);
        return c.getTime();
    }

    @DELETE
    @Path("/{calendarId}/{reservationId}/cancelReservation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancelReservation(@PathParam("calendarId") String calendarId, @PathParam("reservationId") String reservationId) {
        Calendar calendar = this.getCalendarDAO(calendarId);

        Reservation reservation = this.getReservation(reservationId);

        db.delReservation(reservation);
        List<String> calRes = calendar.getReservationsIds();
        calRes.remove(reservationId);
        calendar.setReservationsIds(calRes);
        db.updateCalendar(calendar);

        return Response.ok().entity("Reservation canceled").build();
    }

    @GET
    @Path("/{calendarId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCalendar(@PathParam("calendarId") String calendarId) {
        return Response.ok().entity(this.getCalendarDAO(calendarId).toDTO()).build();
    }

    private Calendar getCalendarDAO(String calId){
        ObjectMapper mapper = new ObjectMapper();
        String objString = jedis.get("Calendars:"+calId); //first tries to get from cache
        Calendar calendar = null;

        if (CACHE_FLAG && objString != null) {
            try {
                calendar = mapper.readValue(objString, Calendar.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        else {
            Iterator<Calendar> itCal = this.db.getCalendarById(calId).iterator();
            if (!itCal.hasNext()) {
                throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("Calendar not found.").build());

            } else calendar = itCal.next();
        }

        return calendar;
    }

    private Reservation getReservation(String resId){
        ObjectMapper mapper = new ObjectMapper();
        String objString = jedis.get("Reservations:"+resId); //first tries to get from cache
        Reservation reservation = null;

        if (CACHE_FLAG && objString != null) {
            try {
                reservation = mapper.readValue(objString, Reservation.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        else {
            Iterator<Reservation> itRes = this.db.getReservationById(resId).iterator();
            if (!itRes.hasNext()) {
                throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("Reservation not found.").build());

            } else reservation = itRes.next();
        }

        return reservation;
    }

}
