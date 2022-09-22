package scc.srv.scc_frontend;

import org.glassfish.jersey.client.ClientConfig;
import scc.data.dtos.EntityDTO;
import scc.data.dtos.ReservationDTO;

import javax.ws.rs.client.*;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Date;
import java.util.List;

public class TestGetReservationsOfTheDay {
    public static void main(String[] args) {

        try {
            String serviceName = "sccappeastasia3phr";
            String hostname = "https://" + serviceName + ".azurewebsites.net/";
            String hostnameLocal = "http://localhost:8080/";
            ClientConfig config = new ClientConfig();
            Client client = ClientBuilder.newClient(config);
            URI baseURI = UriBuilder.fromUri(hostnameLocal).build();
            Long date =  new Date().getTime();
            WebTarget target = client.target(baseURI).path("entity").path(String.valueOf(date)).path("9c943b7a-071e-4ef6-8384-ac5fb5e6e742").path("getReservationsOfTheDay");
            Invocation.Builder invocationBuilder =  target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            List<ReservationDTO> reservations = response.readEntity(new GenericType< List<ReservationDTO>>() {});

            for( ReservationDTO res :  reservations){
                System.out.println(res.demander + " " + res.reservationDate);
            }

            System.out.println(response.getStatus());

        }
        catch (Exception e ){
            e.printStackTrace();
        }
    }
}
