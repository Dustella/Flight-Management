package com.horizon.flight;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.horizon.flight.entities.Flight;
import com.horizon.flight.entities.Passenger;

/**
 * Unit tests for the Flight class.
 */
public class FlightTest {
    private Flight flight;
    private Passenger passenger;

    @BeforeEach
    public void setUp() {
        this.flight = new Flight("FL123", LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(5), "JFK",
                "LAX",
                100);
        this.passenger = new Passenger("John Doe");
    }

    /**
     * Tests the addPassenger method of the Flight class.
     * 
     * This test verifies that a passenger can be successfully added to a flight.
     * It asserts that the addPassenger method returns true and that the size of
     * the passengers list increases to 1 after adding the passenger.
     */
    @Test
    public void testAddPassenger() {

        assertTrue(flight.addPassenger(passenger));
        assertEquals(1, flight.getPassengers().size());
    }

    // This test verifies that an exception is thrown when a passenger is added to a
    // flight that is already full.
    @Test
    public void testAddPassengerWhenFull() {
        for (int i = 0; i < 100; i++) {
            flight.addPassenger(new Passenger("Passenger " + i));
        }
        Exception exception = assertThrows(Exception.class,
                () -> flight.addPassenger(passenger));
        assertNotNull(exception);
    }

    // This test verifies that an exception is thrown when a passenger is added to a
    // flight that is not open for reservation.
    @Test
    public void testAddPassengerWhenNotOpenForReservation() {
        flight.setOpenForReservation(false);
        Exception exception = assertThrows(Exception.class,
                () -> flight.addPassenger(passenger));
        assertNotNull(exception);
    }

    // This test verifies that an exception is thrown when a passenger is added to a
    // flight that has a time conflict with another flight.
    @Test
    public void testRemovePassenger() {
        flight.addPassenger(passenger);
        assertTrue(flight.removePassenger(passenger));
        assertEquals(0, flight.getPassengers().size());
    }

    // This test verifies that an exception is thrown when a passenger is removed
    // from
    // a flight that is not open for reservation.
    @Test
    public void testIsFull() {
        for (int i = 0; i < 100; i++) {
            flight.addPassenger(new Passenger("Passenger " + i));
        }
        assertTrue(flight.isFull());
    }

    // This test verifies that an exception is thrown when a passenger is removed
    // from a flight that is not open for reservation.
    @Test
    public void testHasTimeConflict() {
        LocalDateTime otherDepartureTime = LocalDateTime.now().plusHours(3);
        LocalDateTime otherArrivalTime = LocalDateTime.now().plusHours(6);
        assertTrue(flight.hasTimeConflict(otherDepartureTime, otherArrivalTime));
    }

    @Test
    public void testGetAvailableSeats() {
        flight.addPassenger(passenger);
        assertEquals(99, flight.getAvailableSeats());
    }

    @Test
    public void testCheckAvailability() {
        assertTrue(flight.checkAvailability());
        for (int i = 0; i < 100; i++) {
            flight.addPassenger(new Passenger("Passenger " + i));
        }
        assertFalse(flight.checkAvailability());
    }

    @Test
    public void testSetDepartureTime() {
        LocalDateTime newDepartureTime = LocalDateTime.now().plusHours(3);
        flight.setDepartureTime(newDepartureTime);
        assertEquals(newDepartureTime, flight.getDepartureTime());
    }

    // This test verifies that an exception is thrown when the departure time of a
    // flight is set to a time that is in the past.
    @Test
    public void testSetDepartureTimeInvalid() {
        LocalDateTime newDepartureTime = LocalDateTime.now().minusHours(1);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> flight.setDepartureTime(newDepartureTime));
        assertNotNull(exception);
    }

    @Test
    public void testSetArrivalTime() {
        LocalDateTime newArrivalTime = LocalDateTime.now().plusHours(6);
        flight.setArrivalTime(newArrivalTime);
        assertEquals(newArrivalTime, flight.getArrivalTime());
    }

    // This test verifies that an exception is thrown when the arrival time of a
    // flight is set to a time that is in the past.
    @Test
    public void testSetArrivalTimeInvalid() {
        LocalDateTime newArrivalTime = LocalDateTime.now().minusHours(1);
        Exception exception = assertThrows(Exception.class,
                () -> flight.setArrivalTime(newArrivalTime));
        assertNotNull(exception);
    }
}
