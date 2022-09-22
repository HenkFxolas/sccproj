package scc.srv.scc_frontend;

import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.util.CosmosPagedIterable;
import org.apache.commons.lang3.ArrayUtils;
import org.glassfish.jersey.client.ClientConfig;
import redis.clients.jedis.Jedis;
import scc.data.daos.Hash;
import scc.data.dtos.CalendarDTO;
import scc.data.dtos.EntityDTO;
import scc.srv.db.CosmosDBLayer;
import scc.data.daos.Entity;
import scc.srv.redis.RedisCache;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class TestEntities
{
	public static void main(String[] args) {

		try {
			/*CosmosDBLayer db = CosmosDBLayer.getInstance();
			String id = "testeDBTrigger4";
			CosmosItemResponse<Entity> res = null;
			Entity ent = new Entity();
			ent.setLikes(new LinkedList<>());
			ent.setId(id);
			ent.setForumId("457");
			ent.setDuration(0L);
			ent.setName("SCC " + id);
			ent.setDescription("The best hairdresser");
			ent.setListed(true);
			String[] mediaIds = new String[10];	//String []
			mediaIds[0]="456";
			ent.setMediaIds(mediaIds);
			ent.setNumberOfLikes(0);
			//List<String> calendarIds = new LinkedList<String>();
			//calendarIds.add("456"); //more than one calendar?
			ent.setCalendarId("456");

			res = db.putEntity(ent);
			System.out.println( "Put result");
			System.out.println( res.getStatusCode());
			System.out.println( res.getItem());*/

			/*System.out.println( "Get for id = " + id);
			CosmosPagedIterable<Entity> resGet = db.getEntityById(id);
			for( Entity e: resGet) {
				System.out.println( e);
			}

			System.out.println( "Get for all ids");
			resGet = db.getEntities();
			for( Entity e: resGet) {
				System.out.println( e);
			}*/
			//db.close();

			String serviceName = "sccappwesteurope3phr";
			String container_name = "scc2021-project-0.1";
			String hostname = "https://" + serviceName + ".azurewebsites.net/";
			String hostnameLocal = "http://localhost:8080/";
			String container = "http://scc-container-3phr.westeurope.azurecontainer.io:8080/";
			String loadBalancer = "http://20.73.151.143:80/";
			ClientConfig config = new ClientConfig();
			Client client = ClientBuilder.newClient(config);
			URI baseURI = UriBuilder.fromUri("http://51.137.8.238/scc2021-project-0.1").build();
			//WebTarget target = client.target(baseURI).path("entity");//.path("1bd5e123-3f7a-4122-9339-d3ae7c870814");
			//get
			//target = target.path("2");
			//target = target.path("248ca4aa-80dc-4a9c-8b55-0c05a0835a84");
			CalendarDTO cal = new CalendarDTO(" ", "Calendar2021","Everything is organized", new LinkedList(), 9,0);
			//CalendarDTO cal2 = new CalendarDTO(cal.id, "Calendar2021","Nothing is organized", new LinkedList(),9,18);

			String photo = "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\n" +
					"iRIlSpQocZf4f9dSkyZiGGGdAAAAAElFTkSQmCC";
			byte[] photoB = photo.getBytes();
			List<String> mediaIds = new ArrayList<>();
			String photoId = Hash.of(photoB);
			//mediaIds[0] = photoId;
			EntityDTO ent = new EntityDTO("","HairDresser2021", "Best hairdresser in town 2021", mediaIds,"", true, 60L,"",0);

			//EntityDTO ent2 = new EntityDTO(ent.id, ent.name, ent.description, ent.mediaIds, cal2, ent.listed, ent.duration,"",0);

			/*Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);//.header(HttpHeaders.ACCEPT, "application/json").header(HttpHeaders.CONTENT_TYPE, "application/json");
			Response response = invocationBuilder.post(javax.ws.rs.client.Entity.entity(ent, MediaType.APPLICATION_JSON));

			String entId = response.readEntity(String.class);
			System.out.println(response.getStatus());
			System.out.println(entId);*/

			WebTarget target = client.target(baseURI).path("media");//.path(photoId);
			Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.post(javax.ws.rs.client.Entity.entity(photoB, MediaType.APPLICATION_OCTET_STREAM));
			String blobURL = response.readEntity(String.class);
			System.out.println(response.getStatus());
			System.out.println(blobURL);

			/*WebTarget target = client.target(baseURI).path("media").path(photoId);
			Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_OCTET_STREAM);
			Response response = invocationBuilder.get();
			byte[] contents = response.readEntity(byte[].class);
			System.out.println(response.getStatus());
			System.out.println(new String(contents));*/



		/*	Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.get();
			EntityDTO e = response.readEntity(EntityDTO.class);
			System.out.println(e.description);
			System.out.println(response.getStatus());*/

			//update entity

		/*	EntityDTO ent2 = new EntityDTO("", "novo nome", "Nova desc", null, null, true, 0L,null,0);
			WebTarget target = client.target(baseURI).path("entity").path("bc7b7d73-8323-4cbb-8ae9-2c463e05fe24");
			Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.put(javax.ws.rs.client.Entity.entity(ent2, MediaType.APPLICATION_JSON));
			System.out.println(response.getStatus());*/


			//get entity
																			//f3ba1907-512b-4f1b-9f9f-0cd39f8e826d
			/*WebTarget target1 = client.target(baseURI).path("entity").path("be80872f-b2ad-4eb1-af70-c18b5e8f5936");
			Invocation.Builder invocationBuilder1 = target1.request(MediaType.APPLICATION_JSON);
			Response response1 = invocationBuilder1.get();
			EntityDTO resp1 = response1.readEntity(EntityDTO.class);
			System.out.println(response1.getStatus());
			System.out.println(resp1);*/

			//delete entity

			//WebTarget target = client.target(baseURI).path("entity").path("be80872f-b2ad-4eb1-af70-c18b5e8f5936");
			/*Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.delete();
			String resp = response.readEntity(String.class);
			System.out.println(response.getStatus());
			System.out.println(resp);*/


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


