package com.horizon.flight;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PassengerTest {

    private Passenger passenger;
    private Flight flight;
    private Reservation reservation;
    private List<AddOnService> addOnServices;

    @BeforeEach
    public void setUp() {
        passenger = new Passenger("John Doe");
        flight = new Flight("FL123", LocalDateTime.parse("2023-10-01T10:00:00"),
                LocalDateTime.parse("2023-10-01T14:00:00"), "New York", "Los Angeles", 150);
        addOnServices = new ArrayList<>();
        reservation = new Reservation(flight, TicketCategory.ECONOMY, addOnServices);
    }

    @Test
    public void testPassengerCreation() {
        assertEquals("John Doe", passenger.getName());
        assertFalse(passenger.isLoyaltyMember());
        assertTrue(passenger.getReservations().isEmpty());
    }

    @Test
    public void testJoinLoyaltyScheme() {
        passenger.joinLoyaltyScheme();
        assertTrue(passenger.isLoyaltyMember());
    }

    @Test
    public void testMakeReservation() {
        assertTrue(passenger.makeReservation(flight, TicketCategory.ECONOMY, addOnServices));
        assertEquals(1, passenger.getReservations().size());
    }

    @Test
    public void testMakeReservationWithTimeConflict() {
        passenger.makeReservation(flight, TicketCategory.ECONOMY, addOnServices);
        Flight conflictingFlight = new Flight("FL124", LocalDateTime.parse("2023-10-01T12:00:00"),
                LocalDateTime.parse("2023-10-01T16:00:00"), "New York", "San Francisco", 150);
        assertFalse(passenger.makeReservation(conflictingFlight, TicketCategory.ECONOMY, addOnServices));
    }

    @Test
    public void testCancelReservation() {
        passenger.makeReservation(flight, TicketCategory.ECONOMY, addOnServices);
        assertTrue(passenger.cancelReservation(reservation, 50.0));
        assertTrue(passenger.getReservations().isEmpty());
    }

    // This test verifies that a reservation cannot be canceled if the cancellation
    @Test
    public void testCancelNonExistentReservation() {
        assertFalse(passenger.cancelReservation(reservation, 50.0));
    }

    // This test verifies that a reservation can be canceled by flight.
    @Test
    public void testCancelReservationByFlight() {
        passenger.makeReservation(flight, TicketCategory.ECONOMY, addOnServices);
        passenger.cancelReservationByFlight(flight);
        assertTrue(passenger.getReservations().isEmpty());
    }

    // These following 2 tests test verifies that a reservation cannot be canceled
    // by flight if the flight does not exist.
    @Test
    public void testModifyReservation() {
        passenger.makeReservation(flight, TicketCategory.ECONOMY, addOnServices);
        Flight newFlight = new Flight("FL125", LocalDateTime.parse("2023-10-02T10:00:00"),
                LocalDateTime.parse("2023-10-02T12:00:00"), "New York", "Chicago", 150);
        assertTrue(passenger.modifyReservation(reservation, newFlight, TicketCategory.BUSINESS_CLASS, addOnServices,
                50.0));
        assertEquals(1, passenger.getReservations().size());
        assertEquals(newFlight, passenger.getReservations().get(0).getFlight());
    }

    @Test
    public void testModifyNonExistentReservation() {
        Flight newFlight = new Flight("FL125", LocalDateTime.parse("2023-10-02T10:00:00"),
                LocalDateTime.parse("2023-10-02T12:00:00"), "New York", "Chicago", 150);
        assertFalse(passenger.modifyReservation(reservation, newFlight, TicketCategory.BUSINESS_CLASS, addOnServices,
                50.0));
    }
}