package scc.data.dtos;

import scc.data.daos.Reservation;

import java.util.Date;

public class ReservationDTO {

    public String demander;
    public String establishment;
    public Date reservationDate;
    public String demanderContact;

    public ReservationDTO(String demander, String establishment, Date reservationDate, String demanderContact){
        this.demander = demander;
        this.establishment = establishment;
        this.reservationDate = reservationDate;
        this.demanderContact = demanderContact;
    }

    public ReservationDTO() {
        this.demander = null;
        this.establishment = null;
        this.reservationDate = null;
        this.demanderContact = null;
    }

    public boolean checkFields() {
        return (demander != null && establishment != null && reservationDate != null && this.demanderContact != null);
    }

    public Reservation toDAO(String id, String calendarId) {
        return new Reservation(id,calendarId,this.demander,this.establishment,this.reservationDate, this.demanderContact);
    }
}
