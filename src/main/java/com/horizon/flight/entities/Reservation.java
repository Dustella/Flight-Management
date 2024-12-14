package com.horizon.flight.entities;

import java.io.Serializable;
import java.util.List;

public class Reservation implements Serializable {
    private Flight flight;
    private TicketCategory ticketCategory;
    private List<AddOnService> addOnServices;

    public Reservation(Flight flight, TicketCategory ticketCategory, List<AddOnService> addOnServices) {
        if (flight == null) {
            throw new IllegalArgumentException("main.Flight cannot be null when creating a main.Reservation.");
        }
        this.flight = flight;
        this.ticketCategory = ticketCategory;
        this.addOnServices = addOnServices;
    }

    public Flight getFlight() {
        return flight;
    }

    public TicketCategory getTicketCategory() {
        return ticketCategory;
    }

    public List<AddOnService> getAddOns() {
        return addOnServices;
    }

    // Calculate the total cost of the reservation, including the base price and the
    // cost of all add-on services
    public double calculateTotalCost() {
        double basePrice = ticketCategory.getBasePrice();
        double addOnCost = 0;
        for (AddOnService addOnService : addOnServices) {
            addOnCost += addOnService.getPrice();
        }
        return basePrice + addOnCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Reservation that = (Reservation) o;
        return flight.equals(that.flight) &&
                ticketCategory == that.ticketCategory &&
                addOnServices.equals(that.addOnServices);
    }

    @Override
    public int hashCode() {
        int result = flight.hashCode();
        result = 31 * result + ticketCategory.hashCode();
        result = 31 * result + addOnServices.hashCode();
        return result;
    }
}