package scc.srv.scc_frontend;

import org.glassfish.jersey.client.ClientConfig;
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

public class TestGetFreePeriods {

    public static void main(String[] args) {

        try {
            String hostnameLocal = "http://localhost:8080/";
            ClientConfig config = new ClientConfig();
            Client client = ClientBuilder.newClient(config);
            URI baseURI = UriBuilder.fromUri(hostnameLocal).build();
            Long date =  new Date().getTime();
            WebTarget target = client.target(baseURI).path("entity").path(String.valueOf(date)).path("9c943b7a-071e-4ef6-8384-ac5fb5e6e742").path("getEmptyPeriods");
            Invocation.Builder invocationBuilder =  target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            List<Date> periods = response.readEntity(new GenericType< List<Date>>() {});

            for( Date period : periods)
                System.out.println(period);

            System.out.println(response.getStatus());
        }
        catch (Exception e ){
            e.printStackTrace();
        }
    }

}
