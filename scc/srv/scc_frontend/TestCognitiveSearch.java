package scc.srv.scc_frontend;

import org.glassfish.jersey.client.ClientConfig;
import scc.data.dtos.MessageDTO;
import scc.data.dtos.ReservationDTO;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Date;
import java.util.List;

public class TestCognitiveSearch {


    public static void main(String[] args) {

        try {

            String serviceName = "sccappeastasia3phr";
            String hostname = "https://" + serviceName + ".azurewebsites.net/";
            String hostnameLocal = "http://localhost:8080/";
            ClientConfig config = new ClientConfig();
            Client client = ClientBuilder.newClient(config);
            URI baseURI = UriBuilder.fromUri(hostname).build();
            Long date =  new Date().getTime();
            WebTarget target = client.target(baseURI).path("forum").path("Lampard").path("cognitiveSearchMessage");
            Invocation.Builder invocationBuilder =  target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            List<MessageDTO> messages = response.readEntity(new GenericType< List<MessageDTO>>() {});

            for( MessageDTO message :  messages){
                System.out.println(message.id + " " + message.sender + " " + message.subject + " " + message.message + " " + message.creationTime + " " + message.replyToId + " " + message.forumId);
            }

            System.out.println(response.getStatus());

        }
        catch (Exception e ){
            e.printStackTrace();
        }
    }

}
