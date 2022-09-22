package scc.srv.scc_frontend;

import org.glassfish.jersey.client.ClientConfig;
import scc.data.daos.Message;
import scc.data.dtos.EntityDTO;
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
import java.util.List;

public class TestGetMessages {
    public static void main(String[] args) {

        try{

            String serviceName = "sccappwesteurope3phr";
            String hostname = "https://" + serviceName + ".azurewebsites.net/";
            String hostnameLocal = "http://localhost:8080/";
            ClientConfig config = new ClientConfig();
            Client client = ClientBuilder.newClient(config);
            URI baseURI = UriBuilder.fromUri(hostnameLocal).build();
            WebTarget target = client.target(baseURI).path("forum").path("84a505a1-915c-4ec6-9d73-98995578685b").path("messages");
            Invocation.Builder invocationBuilder =  target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            System.out.println(response.getStatus());
            List<MessageDTO> messages = response.readEntity(new GenericType< List<MessageDTO>>() {});
            System.out.println(response.getStatus());
            for( MessageDTO message :  messages){
                System.out.println(message.sender + " " + message.message);
            }



        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
