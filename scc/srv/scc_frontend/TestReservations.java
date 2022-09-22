package scc.srv.scc_frontend;

import org.glassfish.jersey.client.ClientConfig;
import scc.data.dtos.ReservationDTO;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;

public class TestReservations {
    public static void main(String[] args) {

        try {
            String serviceName = "sccappeastasia3phr";
            String hostname = "https://" + serviceName + ".azurewebsites.net/";
            String hostnameLocal = "http://localhost:8080/";
            ClientConfig config = new ClientConfig();
            Client client = ClientBuilder.newClient(config);
            URI baseURI = UriBuilder.fromUri(hostnameLocal).build();
            String calendarId = "b8ecf6a8-1caa-4667-a25b-3d8cca172c5f";


            Date date = new Date();
            java.util.Calendar c = java.util.Calendar.getInstance();
           /* c.setTime(date);
            c.add(Calendar.HOUR_OF_DAY,1);
            Date finalDate = c.getTime();
            String f =finalDate.toString();*/
            ReservationDTO res = new ReservationDTO("Manuel el Cliente", "Sr Pipo Establishment", date,"1234");

            WebTarget target = client.target(baseURI).path("calendar").path(calendarId).path("addReservation");
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.post(javax.ws.rs.client.Entity.entity(res, MediaType.APPLICATION_JSON));
            String resp = response.readEntity(String.class);
            System.out.println(response.getStatus());
            System.out.println(resp);

            //delete

          /*  target = client.target(baseURI).path("calendar").path(calendarId).path("63204ef3-99ea-4d4b-b3ff-73e50cbaff5e").path("cancelReservation");
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.delete();
            System.out.println(response.getStatus());
            */


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
