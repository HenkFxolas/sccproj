package scc.srv.db;

import com.azure.cosmos.*;
import com.azure.cosmos.models.CosmosItemRequestOptions;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import scc.data.daos.*;
import scc.data.daos.Calendar;
import scc.srv.api.CalendarResource;
import scc.utils.AzureProperties;
import java.util.*;


public class CosmosDBLayer {
	private static final String CONNECTION_URL = AzureProperties.getProperty(AzureProperties.COSMOSDB_URL);
	private static final String DB_KEY = AzureProperties.getProperty(AzureProperties.COSMOSDB_KEY);
	private static final String DB_NAME = AzureProperties.getProperty(AzureProperties.COSMOSDB_DATABASE);
	
	private static CosmosDBLayer instance;

	public static synchronized CosmosDBLayer getInstance() {
		if( instance != null)
			return instance;
		Locale.setDefault(Locale.US); //essential line
		CosmosClient client = new CosmosClientBuilder()
		         .endpoint(CONNECTION_URL)
		         .key(DB_KEY)
		         .directMode()		// comment this is not to use direct mode
		         .consistencyLevel(ConsistencyLevel.SESSION)
		         .connectionSharingAcrossClientsEnabled(true)
		         .contentResponseOnWriteEnabled(true)
		         .buildClient();
		instance = new CosmosDBLayer( client);
		return instance;
		
	}
	
	private CosmosClient client;
	private CosmosDatabase db;
	private CosmosContainer entities;
	private CosmosContainer calendars;
	private CosmosContainer reservations;
	private CosmosContainer forums;
	private CosmosContainer messages;

	public CosmosDBLayer(CosmosClient client) {
		this.client = client;
	}
	
	private synchronized void init() {
		if( db != null)
			return;
		db = client.getDatabase(DB_NAME);
		entities = db.getContainer("Entities");
		calendars = db.getContainer("Calendars");
		reservations = db.getContainer("Reservations");
		forums = db.getContainer("Forums");
		messages = db.getContainer("Messages");

	}

	public CosmosItemResponse<Object> delEntity(Entity entity) {
		init();
		return entities.deleteItem(entity, new CosmosItemRequestOptions());
	}
	
	public CosmosItemResponse<Entity> putEntity( Entity entity) {
		init();
		return entities.createItem(entity);
	}

	public CosmosItemResponse<Entity> updateEntity( Entity entity) {
		init();
		return entities.upsertItem(entity);
	}
	
	public CosmosPagedIterable<Entity> getEntityById( String id) {
		init();
		return entities.queryItems("SELECT * FROM Entities WHERE Entities.id=\"" + id + "\"", new CosmosQueryRequestOptions(), Entity.class);
	}

	public CosmosPagedIterable<Entity> getEntities() {
		init();
		return entities.queryItems("SELECT * FROM Entities ", new CosmosQueryRequestOptions(), Entity.class);
	}

	public CosmosPagedIterable<Entity> mostLikedEntities(){
		init();
		return entities.queryItems("SELECT TOP 5 * FROM Entities WHERE Entities.listed=true ORDER BY Entities.numberOfLikes DESC", new CosmosQueryRequestOptions(), Entity.class);
	}

	public CosmosPagedIterable<Entity> mostRecentEntities() {
		init();
		return entities.queryItems("SELECT TOP 10 * FROM Entities ORDER BY Entities.creationTime DESC", new CosmosQueryRequestOptions(), Entity.class);
	}

	public CosmosItemResponse<Object> delCalendar(Calendar calendar) {
		init();
		return calendars.deleteItem(calendar, new CosmosItemRequestOptions());
	}

	public CosmosItemResponse<Calendar> putCalendar(Calendar calendar) {
		init();
		return calendars.createItem(calendar);
	}

	public CosmosItemResponse<Calendar> updateCalendar(Calendar calendar) {
		init();
		return calendars.upsertItem(calendar);
	}

	public CosmosPagedIterable<Calendar> getCalendarById( String id) {
		init();
		return calendars.queryItems("SELECT * FROM Calendars WHERE Calendars.id=\"" + id + "\"", new CosmosQueryRequestOptions(), Calendar.class);
	}

	public CosmosPagedIterable<Calendar> getCalendars() {
		init();
		return calendars.queryItems("SELECT * FROM Calendars ", new CosmosQueryRequestOptions(), Calendar.class);
	}

	public CosmosItemResponse<Object> delReservation(Reservation reservation) {
		init();
		return reservations.deleteItem(reservation, new CosmosItemRequestOptions());
	}

	public CosmosItemResponse<Reservation> putReservation(Reservation reservation) {
		init();
		return reservations.createItem(reservation);
	}

	public CosmosPagedIterable<Reservation> getReservationById( String id) {
		init();
		return reservations.queryItems("SELECT * FROM Reservations WHERE Reservations.id=\"" + id + "\"", new CosmosQueryRequestOptions(), Reservation.class);
	}

	public CosmosPagedIterable<Reservation> getReservations() {
		init();
		return reservations.queryItems("SELECT * FROM Reservations ", new CosmosQueryRequestOptions(), Reservation.class);
	}

	public CosmosPagedIterable<Reservation> isReservationBooked(String calendarId, Date prev) {
		init();
		return reservations.queryItems("SELECT * FROM Reservations WHERE Reservations.calendarId=\"" + calendarId + "\"" + " AND Reservations.reservationDate=" + prev.getTime(), new CosmosQueryRequestOptions(), Reservation.class);
	}

	public List<Date> getEmptyPeriods(Date date, Calendar calendar ){ //TODO verificar se entidade existe e retornar http caso necessario

		List<Date> periods = new LinkedList<>();
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		Date end = null;
		if(calendar.getClosing() == 0)
			end = CalendarResource.setDate(c,23,59,59,0);
		else
			end = CalendarResource.setDate(c,calendar.getClosing(),0,0,0);

		Date current = CalendarResource.setDate(c, calendar.getOpening(),0,0,0);

		while (current.getTime() < end.getTime()){
			if(!isReservationBooked(calendar.getId(),current).iterator().hasNext())
				periods.add(current);

			c.add(java.util.Calendar.HOUR_OF_DAY,1);
			current = c.getTime();
		}

		return periods;
	}


	public CosmosPagedIterable<Reservation> getReservationsOfTheDay(Date date, Entity ent) {
		String calendarId = ent.getCalendarId();
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		Date prev = CalendarResource.setDate(c,0,0,0,0);
		c.add(java.util.Calendar.DATE, 1);
		Date pos = c.getTime();

		init();

		return reservations.queryItems("SELECT * FROM Reservations WHERE Reservations.reservationDate <="+ pos.getTime() + " AND Reservations.reservationDate >=" + prev.getTime() + " AND Reservations.calendarId=\"" + calendarId + "\"", new CosmosQueryRequestOptions(), Reservation.class);
	}


	public CosmosItemResponse<Object> delForum(Forum forum) {
		init();
		return forums.deleteItem(forum, new CosmosItemRequestOptions());
	}

	public CosmosItemResponse<Forum> putForum(Forum forum) {
		init();
		return forums.createItem(forum);
	}

	public CosmosItemResponse<Forum> updateForum(Forum forum) {
		init();
		return forums.upsertItem(forum);
	}

	public CosmosPagedIterable<Reservation> checkDemanderReservation(String calendarId, String demanderContact){
		init();
		return reservations.queryItems("SELECT * FROM Reservations WHERE Reservations.calendarId=\"" + calendarId + "\"" + " AND Reservations.demanderContact=\"" + demanderContact+"\"", new CosmosQueryRequestOptions(), Reservation.class);
	}

	public CosmosPagedIterable<Forum> getForumById(String id) {
		init();
		return forums.queryItems("SELECT * FROM Forums WHERE Forums.id=\"" + id + "\"", new CosmosQueryRequestOptions(), Forum.class);
	}

	public CosmosPagedIterable<Forum> getForums() {
		init();
		return forums.queryItems("SELECT * FROM Forums ", new CosmosQueryRequestOptions(), Forum.class);
	}

	public CosmosItemResponse<Message> putMessage(Message message) {
		init();
		return messages.createItem(message);
	}

	public CosmosItemResponse<Message> updateMessage(Message message) {
		init();
		return messages.upsertItem(message);
	}

	public CosmosItemResponse<Object> delMessage(Message message) {
		init();
		return messages.deleteItem(message, new CosmosItemRequestOptions());
	}

	public CosmosPagedIterable<Message> getMessages() {
		init();
		return messages.queryItems("SELECT * FROM Messages ", new CosmosQueryRequestOptions(), Message.class);
	}

	public CosmosPagedIterable<Message> getMessageById(String id) {
		init();
		return messages.queryItems("SELECT * FROM Messages WHERE Messages.id=\"" + id + "\"", new CosmosQueryRequestOptions(), Message.class);
	}

	public CosmosPagedIterable<Message> getForumMessage(String id, String forumId){
		init();
		return messages.queryItems("SELECT * FROM Messages WHERE Messages.id=\"" + id + "\"" + " AND Messages.forumId=\"" + forumId+"\"", new CosmosQueryRequestOptions(), Message.class);
	}

	public CosmosPagedIterable<Message> getMessageReply(String id){
		init();
		return messages.queryItems("SELECT * FROM Messages WHERE Messages.replyToId=\"" + id + "\"", new CosmosQueryRequestOptions(), Message.class);
	}


	public CosmosPagedIterable<Message> getMessagesWithoutReply() {
		init();
		return messages.queryItems("SELECT * FROM Messages WHERE Messages.replyToId=null OR Messages.replyToId=\"\"", new CosmosQueryRequestOptions(), Message.class);
	}

	public CosmosPagedIterable<Message> getForumMessages(String forumId) {
		return messages.queryItems("SELECT * FROM Messages WHERE Messages.forumId=\"" + forumId + "\"", new CosmosQueryRequestOptions(), Message.class);
	}

	public void close() {
		client.close();
	}

	
}
