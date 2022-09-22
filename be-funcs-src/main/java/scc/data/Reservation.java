package scc.data;

import java.util.Date;

public class Reservation {

    private String id;
    private String calendarId;
    private String demander;
    private String establishment;
    private Date reservationDate;
    private String demanderContact;

    public Reservation() {
        this.calendarId = null;
        this.id = null;
        this.demander = null;
        this.establishment = null;
        this.reservationDate = null;
        this.demanderContact = null;
    }

    public Reservation(String id, String calendarId, String demander, String establishment, Date reservationDate, String demanderContact) {
        this.calendarId = calendarId;
        this.id = id;
        this.demander = demander;
        this.establishment = establishment;
        this.reservationDate = reservationDate;
        this.demanderContact = demanderContact;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDemander() {
        return demander;
    }

    public void setDemander(String demander) {
        this.demander = demander;
    }

    public String getEstablishment() {
        return establishment;
    }

    public void setEstablishment(String establishment) {
        this.establishment = establishment;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public String getDemanderContact() {
        return demanderContact;
    }

    public void setDemanderContact(String demanderContact) {
        this.demanderContact = demanderContact;
    }

}
