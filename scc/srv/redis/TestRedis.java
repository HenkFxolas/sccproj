package scc.srv.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;
import scc.data.daos.Entity;
import scc.data.daos.Message;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class TestRedis
{
	public static void main(String[] args) {

		/*try {
			ObjectMapper mapper = new ObjectMapper();

			String id = "0" + System.currentTimeMillis();
			Entity ent = new Entity();
			ent.setId(id);
			ent.setName("SCC " + id);
			ent.setDescription("The best hairdresser");
			ent.setListed(true);
			List<String> mIds = new LinkedList<String>();
			mIds.add("456");
			//ent.setMediaIds(mIds);
			ent.setCalendarId("456");

			try (Jedis jedis = RedisCache.getCachePool().getResource()) {
			    Long cnt = jedis.lpush("MostRecentEntities", mapper.writeValueAsString(ent));
			    if (cnt > 5)
			        jedis.ltrim("MostRecentEntities", 0, 4);
			    
			    List<String> lst = jedis.lrange("MostRecentEntities", 0, -1);
			    for( String s : lst)
			    	System.out.println(s);

			    cnt = jedis.incr("NumEntities");
			    System.out.println( "Num entities : " + cnt);
			}
			


		} catch (Exception e) {
			e.printStackTrace();
		}*/
		try (Jedis jedis = RedisCache.getCachePool().getResource()) {
			/*ObjectMapper mapper = new ObjectMapper();

			String id = "1AAAAAIJAIJSAI";
			Message message = new Message();
			message.setId(id);
			List<Message> msgs = new LinkedList<>();
			msgs.add(message);
			try {
				jedis.rpush("serverless::cosmos::msgs2321", new ObjectMapper().writeValueAsString(msgs));
			} catch (JsonProcessingException e) {
				System.out.println("allaslas");
			}*/


		/*	String id = "0" + System.currentTimeMillis();
			Entity ent = new Entity();
			ent.setId(id);
			ent.setName("SCC " + id);
			ent.setDescription("The best hairdresser");
			ent.setListed(true);
			String[] mIds = new String[10];
			mIds[0] = "457";
			ent.setMediaIds(mIds);
			ent.setCalendarId("456");

			jedis.set("1",mapper.writeValueAsString(ent));
			String v1 = jedis.get("1");
			Entity e1 = mapper.readValue(v1,Entity.class);
			System.out.println(e1.toString());
			Long del2 = jedis.del("1");
			v1 = jedis.get("1");

			System.out.println(v1);*/
			/*String listPop = jedis.get("Entities:bc7b7d73-8323-4cbb-8ae9-2c463e05fe24");
			System.out.println(listPop);
			String cal = jedis.get("Calendars:7226af64-8a1d-47cc-93e5-41e93fbe15f0");
			System.out.println(cal);*/

			/*Message mess = new Message("99fe62c9-645b-4b67-b2a4-2f6fc1804ce9","Pogba",new Date(1606575924537L),"Game","It was a very good game","","dc826c74-657a-4cc9-b9dc-a10fa4291246");
			Long a = jedis.lrem("MessagesOnForum:dc826c74-657a-4cc9-b9dc-a10fa4291246", 1 ,  new ObjectMapper().writeValueAsString(mess));
			System.out.println(a);*/
		/*	List<String> messages = jedis.lrange("MessagesOnForum:dc826c74-657a-4cc9-b9dc-a10fa4291246",0,-1);
			messages.stream().forEach(message-> System.out.println(message));*/
			/*String r = jedis.get("Reservations:35247412-ff42-4b95-9e65-73ff60582003");
			System.out.println(r);*/
		}

	}
}


