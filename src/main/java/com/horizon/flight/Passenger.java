package com.horizon.flight;

import java.util.ArrayList;
import java.util.List;

public class Passenger {
//
    public  static Integer passengerCount = 0;
    private String name;
    private List<Reservation> reservations;
    private  Integer ID;
    private boolean isLoyaltyMember;

    public Passenger(String name) {
        this.name = name;
        passengerCount+=1;
        this.ID = passengerCount;
        this.reservations = new ArrayList<>();
        this.isLoyaltyMember = false;
    }


    public String getName() {
        return name;
    }

    // Get all reservations of the passenger
    public List<Reservation> getReservations() {
        return reservations;
    }

    // Check if the passenger is a loyalty member
    public boolean isLoyaltyMember() {
        return isLoyaltyMember;
    }

    // 乘客加入忠诚度计划
    public void joinLoyaltyScheme() {
        isLoyaltyMember = true;
    }

    // Check for any time conflicts with existing reservations
    public boolean makeReservation(Flight f, TicketCategory category, List<AddOnService> addOnServices) {
        if (!f.isOpenForReservation()) {
            System.out.println("main.Flight is not open for reservation.");
            return false;
        }
        for (Reservation reservation : reservations) {
            if (f.hasTimeConflict(reservation.getFlight().getDepartureTime(), reservation.getFlight().getArrivalTime())) {
                System.out.println("Time conflict with existing reservation.");
                return false;
            }
        }
        Reservation newReservation = new Reservation(f, category, addOnServices); // 确保这里传入的 f 不为 null
        reservations.add(newReservation);
        return f.addPassenger(this);
    }

    // Cancel a reservation
    public boolean cancelReservation(Reservation r, double fee) {
        if (reservations.remove(r)) {
            r.getFlight().removePassenger(this);
            // TODO: we can add logic to deduct fee from account balance, but we will skip it for now
            return true;
        }
        return false;
    }

    // called when a flight is cancelled, remove all reservations for the flight
    public void cancelReservationByFlight(Flight f) {
        List<Reservation> toRemove = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getFlight().equals(f)) {
                toRemove.add(reservation);
            }
        }
        for (Reservation reservation : toRemove) {
            reservations.remove(reservation);
        }
    }

//    modify current reservation, remove passenger from current flight, add passenger to new flight
    public boolean modifyReservation(Reservation r, Flight newFlight, TicketCategory newCategory, List<AddOnService> newAddOnServices, double fee) {
        int index = reservations.indexOf(r);
        if (index == -1) {
            System.out.println("main.Reservation not found.");
            return false;
        }

        // First remove the passenger from the current flight
        r.getFlight().removePassenger(this);

        // Create a new reservation with the updated flight, category and add-on services
        Reservation updatedReservation = new Reservation(newFlight, newCategory, newAddOnServices);

        // Update the reservation in the list
        reservations.set(index, updatedReservation);

        // Add the passenger to the new flight, if not successful, revert the reservation
        if (!newFlight.addPassenger(this)) {
            System.out.println("Failed to add passenger to the new flight.");
            return false;
        }

        return true;
    }

    public Integer getID() {
        return ID;
    }

}