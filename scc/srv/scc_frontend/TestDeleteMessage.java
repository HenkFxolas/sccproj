package scc.srv.scc_frontend;

import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class TestDeleteMessage {

    public static void main(String[] args) {

        try {
            String hostnameLocal = "http://localhost:8080/";
            ClientConfig config = new ClientConfig();
            Client client = ClientBuilder.newClient(config);
            URI baseURI = UriBuilder.fromUri(hostnameLocal).build();
            WebTarget target = client.target(baseURI).path("forum").path("dc826c74-657a-4cc9-b9dc-a10fa4291246").path("6cce0771-8ed0-4e0e-aca4-782d633c0635");
            Invocation.Builder invocationBuilder =  target.request();
            Response response = invocationBuilder.delete();
            System.out.println(response.getStatus());
        }
        catch (Exception e ){
            e.printStackTrace();
        }
    }

}
