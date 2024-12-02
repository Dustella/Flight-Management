package com.horizon.flight.cmd;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.horizon.flight.entities.AddOnService;
import com.horizon.flight.entities.AirlineCompany;
import com.horizon.flight.entities.Flight;
import com.horizon.flight.entities.Passenger;
import com.horizon.flight.entities.TicketCategory;

public class MainMenu implements CLIInteraction {
    private AirlineCompany airline;
    private Scanner scanner;

    public void CLIInteractionImpl(AirlineCompany airline) {
        this.airline = airline;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void start() {
        while (true) {
            System.out.println("1. Add Flight");
            System.out.println("2. Add Passenger");
            System.out.println("3. Make Reservation");
            System.out.println("4. Delay Flight");
            System.out.println("5. Cancel Flight");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addFlight();
                case 2 -> addPassenger();
                case 3 -> makeReservation();
                case 4 -> delayFlight();
                case 5 -> cancelFlight();
                case 6 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addFlight() {
        System.out.print("Enter flight number: ");
        String flightNumber = scanner.nextLine();
        System.out.print("Enter departure time (yyyy-MM-ddTHH:mm:ss): ");
        LocalDateTime departureTime = LocalDateTime.parse(scanner.nextLine());
        System.out.print("Enter arrival time (yyyy-MM-ddTHH:mm:ss): ");
        LocalDateTime arrivalTime = LocalDateTime.parse(scanner.nextLine());
        System.out.print("Enter departure airport: ");
        String departureAirport = scanner.nextLine();
        System.out.print("Enter arrival airport: ");
        String arrivalAirport = scanner.nextLine();
        System.out.print("Enter capacity: ");
        int capacity = scanner.nextInt();
        scanner.nextLine(); // consume newline

        Flight flight = new Flight(flightNumber, departureTime, arrivalTime, departureAirport, arrivalAirport,
                capacity);
        airline.addFlight(flight);
        System.out.println("Flight added successfully.");
    }

    private void addPassenger() {
        System.out.print("Enter passenger name: ");
        String name = scanner.nextLine();
        Passenger passenger = new Passenger(name);
        System.out.println("Passenger added successfully.");
    }

    private void makeReservation() {
        System.out.print("Enter flight number: ");
        String flightNumber = scanner.nextLine();
        Flight flight = airline.getFlightByNumber(flightNumber);
        if (flight == null) {
            System.out.println("Flight not found.");
            return;
        }

        System.out.print("Enter passenger name: ");
        String name = scanner.nextLine();
        Passenger passenger = new Passenger(name);

        System.out.print("Enter ticket category (ECONOMY/BUSINESS_CLASS): ");
        TicketCategory category = TicketCategory.valueOf(scanner.nextLine().toUpperCase());

        List<AddOnService> addOnServices = new ArrayList<>();
        System.out.print("Enter number of add-on services: ");
        int numServices = scanner.nextInt();
        scanner.nextLine(); // consume newline
        for (int i = 0; i < numServices; i++) {
            System.out.print("Enter add-on service name: ");
            String serviceName = scanner.nextLine();
            System.out.print("Enter add-on service price: ");
            double price = scanner.nextDouble();
            scanner.nextLine(); // consume newline
            addOnServices.add(new AddOnService(serviceName, price));
        }

        passenger.makeReservation(flight, category, addOnServices);
        System.out.println("Reservation made successfully.");
    }

    private void delayFlight() {
        System.out.print("Enter flight number: ");
        String flightNumber = scanner.nextLine();
        Flight flight = airline.getFlightByNumber(flightNumber);
        if (flight == null) {
            System.out.println("Flight not found.");
            return;
        }

        System.out.print("Enter delay in minutes: ");
        int delayMinutes = scanner.nextInt();
        scanner.nextLine(); // consume newline

        airline.delayFlight(flight, delayMinutes);
        System.out.println("Flight delayed successfully.");
    }

    private void cancelFlight() {
        System.out.print("Enter flight number: ");
        String flightNumber = scanner.nextLine();
        Flight flight = airline.getFlightByNumber(flightNumber);
        if (flight == null) {
            System.out.println("Flight not found.");
            return;
        }

        airline.cancelFlight(flight);
        System.out.println("Flight canceled successfully.");
    }
}
