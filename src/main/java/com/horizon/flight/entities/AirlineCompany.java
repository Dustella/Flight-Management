package com.horizon.flight.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AirlineCompany implements Serializable {
    private final String name;
    private final List<Flight> flights;

    public AirlineCompany(String name) {
        this.name = name;
        this.flights = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void addFlight(Flight f) {
        if (f == null) {
            throw new IllegalArgumentException("main.Flight cannot be null when adding to main.AirlineCompany.");
        }
        flights.add(f);
    }

    // Remove a flight from the airline company, and cancel reservations for all
    // passengers
    public void cancelFlight(Flight f) {
        if (f == null) {
            throw new IllegalArgumentException("main.Flight cannot be null when cancelling from main.AirlineCompany.");
        }

        for (Passenger passenger : f.getPassengers()) {
            passenger.cancelReservationByFlight(f);
        }
        flights.remove(f);
    }

    // Delay a flight by a given number of minutes, and notify passengers
    public void delayFlight(Flight f, int minutes) {
        LocalDateTime newDepartureTime = f.getDepartureTime().plusMinutes(minutes);
        LocalDateTime newArrivalTime = f.getArrivalTime().plusMinutes(minutes);
        f.setDepartureTime(newDepartureTime);
        f.setArrivalTime(newArrivalTime);

        // 通知乘客航班延迟，这里简单打印消息，实际可替换为合适的通知机制
        for (Passenger passenger : f.getPassengers()) {
            System.out.println("Dear " + passenger.getName() + ", your flight " + f.getFlightNumber() + " from "
                    + f.getDepartureAirport() + " to " + f.getArrivalAirport() + " has been delayed by " + minutes
                    + " minutes.");
        }
    }

    // Check flights with capacity utilization greater than or equal to the given
    // threshold percentage
    public List<Flight> checkCapacityUtilization(int thresholdPercentage) {
        // do checks for input percentage
        if (thresholdPercentage < 0 || thresholdPercentage > 100) {
            throw new IllegalArgumentException("Invalid threshold percentage.");
        }

        List<Flight> nearFullFlights = new ArrayList<>();

        for (Flight flight : flights) {
            double utilization = (double) flight.getPassengers().size() / flight.getCapacity();
            if (utilization >= (thresholdPercentage / 100.0)) {
                nearFullFlights.add(flight);
            }
        }
        return nearFullFlights;
    }

    // Find a flight by its flight number
    public Flight findFlightByNumber(String flightNumber) {
        for (Flight flight : flights) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                return flight;
            }
        }
        return null;
    }

    public boolean cancelFlightByNumber(String flightNumber) {
        if (flightNumber == null || flightNumber.isEmpty()) {
            throw new IllegalArgumentException("Flight number cannot be null or empty.");
        }

        // Find the flight by its flight number
        for (Flight flight : flights) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                // Cancel the flight and remove it from the list
                cancelFlight(flight);
                return true; // Return true if the flight was successfully cancelled
            }
        }

        return false; // Return false if no flight with the given flight number was found
    }

}