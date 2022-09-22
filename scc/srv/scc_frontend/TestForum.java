package scc.srv.scc_frontend;

import org.glassfish.jersey.client.ClientConfig;
import scc.data.dtos.MessageDTO;

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

public class TestForum {
    public static void main(String[] args) {

        try {

            String hostnameLocal = "http://localhost:8080/";
            ClientConfig config = new ClientConfig();
            Client client = ClientBuilder.newClient(config);
            URI baseURI = UriBuilder.fromUri(hostnameLocal).build();
            String forumId = "f0797400-0ed3-43b5-b702-832c841d495e";
          //  String messageId = "d4a7bf51-2e2e-462e-a98e-5d287dd77cf9";

            Date date = new Date();

            MessageDTO message = new MessageDTO(null, "Ronaldo1", date, "Game", "It was a very good game", "",forumId);

            WebTarget target = client.target(baseURI).path("forum").path(forumId).path("addMessage");
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.post(javax.ws.rs.client.Entity.entity(message, MediaType.APPLICATION_JSON));

            String messageId = response.readEntity(String.class);
            System.out.println(response.getStatus());
            System.out.println(messageId);



            /*/target = client.target(baseURI).path("forum").path(messageId).path("replyMessage");
            Invocation.Builder invocationBuilder2 = target.request(MediaType.APPLICATION_JSON);
            Response response2 = invocationBuilder2.post(javax.ws.rs.client.Entity.entity("Chupa teresa", MediaType.APPLICATION_JSON));

            System.out.println(response2.getStatus());*/


            //get messages
        /*    WebTarget target = client.target(baseURI).path("forum").path("0d217b9f-fb09-4762-8208-98ae8dcb189e").path("messages");
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();

            List<MessageDTO> messages = response.readEntity(new GenericType<List<MessageDTO>>(){});
            messages.stream().forEach(m -> System.out.println(m.sender+" "+ m.id));
            System.out.println(response.getStatus());*/



        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
