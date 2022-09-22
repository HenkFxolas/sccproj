package scc.srv.scc_frontend;

import org.glassfish.jersey.client.ClientConfig;
import scc.data.dtos.EntityDTO;

import javax.ws.rs.client.*;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

public class TestAddMediaID {
    public static void main(String[] args) {

        try {
            String hostnameLocal = "http://localhost:8080/";
            ClientConfig config = new ClientConfig();
            Client client = ClientBuilder.newClient(config);
            URI baseURI = UriBuilder.fromUri(hostnameLocal).build();
            String mediaId = "123A03";
            WebTarget target = client.target(baseURI).path("entity").path("89eeac31-5603-4e45-bd54-d6c1bbec9f59").path(mediaId).path("addMediaId");
            Invocation.Builder invocationBuilder = target.request();
            Entity<?> empty = Entity.text("");
            Response response = invocationBuilder.put(empty);
            System.out.println(response.getStatus());
            String id = response.readEntity(String.class);
            System.out.println(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
