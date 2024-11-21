/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.horizon.flight;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FlightManagement {

    public static void main(String[] args) {


        AirlineCompany airline = new AirlineCompany("China Eastern Airlines");

        // Create a flight
        LocalDateTime departureTime = LocalDateTime.now().plusHours(2);
        LocalDateTime arrivalTime = departureTime.plusHours(3);
        Flight flight1 = new Flight("MU123", departureTime, arrivalTime, "Shanghai", "Beijing", 100);
        airline.addFlight(flight1);

        // Init Passengers
        Passenger passenger1 = new Passenger("John Doe");
        Passenger passenger2 = new Passenger("Jane Smith");

        // Init AddOnServices
        List<AddOnService> addOnServices = new ArrayList<>();
        addOnServices.add(new AddOnService("Speedy Boarding", 10));
        addOnServices.add(new AddOnService("Seat Selection", 20));

        // Make reservations
        TicketCategory category = TicketCategory.ECONOMY;
        if (flight1!= null) {
            passenger1.makeReservation(flight1, category, addOnServices);
        }

        // Delay flight
        airline.delayFlight(flight1, 30);

        // Modify reservation ( demonstrate error case )
        Flight newFlight = new Flight("MU456", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(4), "Shanghai", "Guangzhou", 100);
        TicketCategory newCategory = TicketCategory.BUSINESS_CLASS;
        List<AddOnService> newAddOnServices = new ArrayList<>();
        if (passenger1.getReservations().size() > 0) {
            Reservation reservationToModify = passenger1.getReservations().get(0);
            boolean modifyResult = passenger1.modifyReservation(reservationToModify, newFlight, newCategory, newAddOnServices, 50);
            if (!modifyResult) {
                System.out.println("Failed to modify reservation.");
            }
        }

        // Modify reservation ( demonstrate success case )
        newFlight = new Flight("MU123", flight1.getDepartureTime().plusHours(1), flight1.getArrivalTime().plusHours(1), "Shanghai", "Beijing", 100);
        if (passenger1.getReservations().size() > 0) {
            Reservation reservationToModify = passenger1.getReservations().get(0);
            boolean modifyResult = passenger1.modifyReservation(reservationToModify, newFlight, newCategory, newAddOnServices, 50);
            if (modifyResult) {
                System.out.println("main.Reservation modified successfully.");
            }
        }

        // Check near full flights ( >= 80% capacity )
        List<Flight> nearFullFlights = airline.checkCapacityUtilization(80);
        for (Flight f : nearFullFlights) {
            System.out.println("main.Flight " + f.getFlightNumber() + " from " + f.getDepartureAirport() + " to " + f.getArrivalAirport() + " is near full.");
        }

        // Cancel flight
        airline.cancelFlight(flight1);

        // Try to cancel non-existent reservation ( demonstrate error case ), create a dummy Flight object to construct Reservation
        Flight dummyFlight = new Flight("DummyFlightNumber", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "DummyDeparture", "DummyArrival", 1);
        Reservation nonExistentReservation = new Reservation(dummyFlight, category, addOnServices);
        boolean cancelResult = passenger1.cancelReservation(nonExistentReservation, 10);
        if (!cancelResult) {
            System.out.println("Failed to cancel non-existent reservation.");
        }

    }
}
