package scc.data.dtos;

import java.util.List;

public class CalendarDTO {

    public String id, name, description;
    public List<String> reservations;
    public int opening, closing;

    public CalendarDTO(String id, String name, String description, List<String> reservations, int opening, int closing){
        this.id = id;
        this.name = name;
        this.description = description;
        this.reservations = reservations;
        this.opening = opening;
        this.closing = closing;

    }

    public CalendarDTO() {
        this.id = null;
        this.name = null;
        this.description = null;
        this.reservations = null;
        this.opening = -1;
        this.closing = -1;
    }


}
