package scc.srv.scc_frontend;

import org.glassfish.jersey.client.ClientConfig;
import scc.data.dtos.EntityDTO;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

public class TestMostRecentEntites {

    public static void main(String[] args) {

        try{
            String hostnameLocal = "http://localhost:8080/";
            ClientConfig config = new ClientConfig();
            Client client = ClientBuilder.newClient(config);
            URI baseURI = UriBuilder.fromUri(hostnameLocal).build();
            WebTarget target = client.target(baseURI).path("entity/getMostRecent");
            Invocation.Builder invocationBuilder =  target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            System.out.println(response.getStatus());
            List<EntityDTO> ents = response.readEntity(new GenericType< List<EntityDTO>>() {});
            System.out.println(response.getStatus());
            for( EntityDTO ent :  ents){
                System.out.println(ent.id);
            }



        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
