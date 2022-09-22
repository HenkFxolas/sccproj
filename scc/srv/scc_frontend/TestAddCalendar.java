package scc.srv.scc_frontend;

import org.glassfish.jersey.client.ClientConfig;
import scc.data.dtos.CalendarDTO;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.LinkedList;

public class TestAddCalendar {

    public static void main(String[] args) {

        try {
            String hostnameLocal = "http://localhost:8080/";

            String serviceName = "sccappwesteurope3phr";

            String hostname = "https://" + serviceName + ".azurewebsites.net/";
            ClientConfig config = new ClientConfig();
            Client client = ClientBuilder.newClient(config);
            URI baseURI = UriBuilder.fromUri(hostnameLocal).build();
            WebTarget target = client.target(baseURI).path("entity").path("ea221a7f-3b40-46d4-abc7-e9df1e89fca9");
            CalendarDTO cal = new CalendarDTO(" ", "Calendar2021","Everything is organized", new LinkedList(), 9,0);
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);//.header(HttpHeaders.ACCEPT, "application/json").header(HttpHeaders.CONTENT_TYPE, "application/json");
            Response response = invocationBuilder.post(javax.ws.rs.client.Entity.entity(cal, MediaType.APPLICATION_JSON));

            String entId = response.readEntity(String.class);
            System.out.println(response.getStatus());
            System.out.println(entId);

        }
        catch (Exception e ){
            e.printStackTrace();
        }
    }

}
