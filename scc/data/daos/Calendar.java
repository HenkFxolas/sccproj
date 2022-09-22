package scc.data.daos;

import scc.data.dtos.CalendarDTO;
import scc.data.dtos.EntityDTO;
import scc.data.dtos.ReservationDTO;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Calendar {

    private String id;
    private String name;
    private String description;
    private List<String> reservationsIds;
    private int opening;
    private int closing;

    public Calendar() {
        this.id = null;
        this.name = null;
        this.description = null;
        this.reservationsIds = null;
        this.opening = -1;
        this.closing = -1;
    }

    public Calendar(String id, String name, String description, List<String> reservationsIds, int opening, int closing) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.reservationsIds = reservationsIds;
        this.opening = opening;
        this.closing = closing;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getReservationsIds() {
        return reservationsIds;
    }

    public CalendarDTO toDTO(){
        return new CalendarDTO(this.id, this.name, this.description, this.reservationsIds, this.opening, this.closing);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setReservationsIds(List<String> reservationsIds) {
        this.reservationsIds = reservationsIds;
    }

    public int getOpening() {
        return opening;
    }

    public void setOpening(Integer opening) {
        this.opening = opening;
    }

    public int getClosing() {
        return closing;
    }

    public void setClosing(int closing) {
        this.closing = closing;
    }
}
