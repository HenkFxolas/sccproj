package scc.srv.scc_frontend;

import org.glassfish.jersey.client.ClientConfig;
import scc.data.dtos.EntityDTO;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class TestLike {

    public static void main(String[] args) {

        try {
            String hostnameLocal = "http://localhost:8080/";
            ClientConfig config = new ClientConfig();
            Client client = ClientBuilder.newClient(config);
            URI baseURI = UriBuilder.fromUri(hostnameLocal).build();
            WebTarget target = client.target(baseURI).path("entity").path("9c943b7a-071e-4ef6-8384-ac5fb5e6e742").path("like").path("true").path("1234");
            Invocation.Builder invocationBuilder =  target.request();
            Entity<?> empty = Entity.text("");
            Response response = invocationBuilder.put(empty);
            System.out.println(response.getStatus());
        }
        catch (Exception e ){
            e.printStackTrace();
        }
    }
}
