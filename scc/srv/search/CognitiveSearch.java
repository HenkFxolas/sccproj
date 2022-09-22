package scc.srv.search;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.glassfish.jersey.client.ClientConfig;
import scc.utils.AzureProperties;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class CognitiveSearch {

    private static final String SERVICE_NAME = AzureProperties.getProperty(AzureProperties.SEARCH_SERVICE_NAME);
    private static final String QUERY_KEY = AzureProperties.getProperty(AzureProperties.SEARCH_QUERY_KEY);


    public static void main(String[] args) { //for testing
        searchMessages("tomates");
    }
    public static JsonObject searchMessages(String search){

        JsonObject resultObj = new JsonObject();
        try {
            String hostname = "https://" + SERVICE_NAME + ".search.windows.net/";
            ClientConfig config = new ClientConfig();
            Client client = ClientBuilder.newClient(config);
            URI baseURI = UriBuilder.fromUri(hostname).build();
            WebTarget target = client.target(baseURI);
            String index = "cosmosdb-index";

            JsonObject obj = new JsonObject();
            obj.addProperty("count", "true");
            obj.addProperty("search", search);
            obj.addProperty("searchFields", "sender,message,subject");
            obj.addProperty("searchMode", "all");
            obj.addProperty("queryType", "full");
            obj.addProperty("select", "id,sender,creationTime,subject,message,replyToId,forumId");


            String resultStr = target.path("indexes/" + index + "/docs/search").queryParam("api-version", "2019-05-06")
                    .request().header("api-key", QUERY_KEY)
                    .accept(MediaType.APPLICATION_JSON).post(Entity.entity(obj.toString(), MediaType.APPLICATION_JSON))
                    .readEntity(String.class);

            resultObj = new Gson().fromJson(resultStr, JsonObject.class);
            System.out.println(resultObj);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return  resultObj;
    }
}
