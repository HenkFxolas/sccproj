package scc.serverless;

import java.text.SimpleDateFormat;
import java.util.*;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosItemRequestOptions;
import com.microsoft.azure.functions.annotation.*;

import redis.clients.jedis.Jedis;
import scc.data.Entity;
import scc.data.Reservation;
import scc.srv.CosmosDBFactory;
import scc.srv.RedisCache;
import scc.utils.AzureProperties;

import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with Timer Trigger.
 */
public class TimerFunction {

	@FunctionName("update-popular-entities")										// every 12 hours
	public void cosmosTimerFunction1( @TimerTrigger(name = "keepAliveTriggerPopular", schedule = "0 0 */12 * * *") String timerInfo,
								ExecutionContext context) {

		try (Jedis jedis = RedisCache.getCache().getJedisPool().getResource()) {
			jedis.set("serverlesstime", new SimpleDateFormat().format(new Date()));

			try {

				//get top 5 liked entities
				CosmosPagedIterable<Entity> it = CosmosDBFactory.getCosmosClient().getDatabase(AzureProperties.getProperty(AzureProperties.COSMOSDB_DATABASE))
						.getContainer("Entities").queryItems("SELECT TOP 5 * FROM Entities WHERE Entities.listed=true ORDER BY Entities.numberOfLikes DESC",
								new CosmosQueryRequestOptions(), Entity.class);

				List<Entity> lst = new ArrayList<>();
				it.stream().forEach( e -> lst.add(e));

				jedis.set("serverless:cosmos:PopularEntities", new ObjectMapper().writeValueAsString(lst));
			} catch (Exception e) {
				jedis.set("serverless:cosmos:PopularEntities", "[]");
			}
		}
	}

	@FunctionName("update-recent-entities")										// every hour
	public void cosmosTimerFunction2( @TimerTrigger(name = "keepAliveTriggerRecent", schedule = "0 0 */1 * * *") String timerInfo,
								ExecutionContext context) {

		try (Jedis jedis = RedisCache.getCache().getJedisPool().getResource()) {
			jedis.set("serverlesstime", new SimpleDateFormat().format(new Date()));

			try {

				//get top 10 liked entities
				CosmosPagedIterable<Entity> it = CosmosDBFactory.getCosmosClient().getDatabase(AzureProperties.getProperty(AzureProperties.COSMOSDB_DATABASE))
						.getContainer("Entities").queryItems("SELECT TOP 10 * FROM Entities ORDER BY Entities.creationTime DESC",
								new CosmosQueryRequestOptions(), Entity.class);

				List<Entity> lst = new ArrayList<>();
				it.stream().forEach( e -> lst.add(e));

				jedis.set("serverless:cosmos:RecentEntities", new ObjectMapper().writeValueAsString(lst));
			} catch (Exception e) {
				jedis.set("serverless:cosmos:RecentEntities", "[]");
			}
		}
	}

	@FunctionName("delete-reservations-day")		// every day five minutes before midnight deletes reservations of the day or can be seen as deleting past reservations until now
	public void cosmosTimerFunction3( @TimerTrigger(name = "keepAliveTriggerReservations", schedule = "0 55 23 */1 * *") String timerInfo,
									  ExecutionContext context) {

		try (Jedis jedis = RedisCache.getCache().getJedisPool().getResource()) {
			jedis.set("serverlesstime", new SimpleDateFormat().format(new Date()));

			try {
				Long now = new Date().getTime();

				CosmosContainer reservations = CosmosDBFactory.getCosmosClient().getDatabase(AzureProperties.getProperty(AzureProperties.COSMOSDB_DATABASE))
						.getContainer("Reservations");

				CosmosPagedIterable<Reservation> it = reservations.queryItems("SELECT * FROM Reservations WHERE Reservations.reservationDate <" + now, new CosmosQueryRequestOptions(), Reservation.class);

				it.stream().forEach(r -> {
					jedis.del("Reservations:"+r.getId());
					reservations.deleteItem(r, new CosmosItemRequestOptions());
				});


			} catch (Exception e) {

			}
		}
	}
}
