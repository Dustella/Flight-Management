package com.horizon.flight.cmd.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.horizon.flight.cmd.Storage;
import com.horizon.flight.entities.AddOnService;
import com.horizon.flight.entities.Flight;
import com.horizon.flight.entities.Passenger;
import com.horizon.flight.entities.Reservation;
import com.horizon.flight.entities.TicketCategory;

public class PassengerMenu implements Menu {
    private final Map<String, Command> commands = new HashMap<>();

    public PassengerMenu() {
        commands.put("search", new Command() {
            @Override
            public String execute(String... args) {
                if (args.length < 2) {
                    return "Usage: search <departureAirport> <arrivalAirport>";
                }
                String departureAirport = args[0];
                String arrivalAirport = args[1];
                StringBuilder result = new StringBuilder(
                        "Flights from " + departureAirport + " to " + arrivalAirport + ":\n");
                boolean found = false;
                var allAirlineCompanies = Storage.getInstance().getAllAirlineCompanies();
                for (var airlineCompany : allAirlineCompanies.values()) {
                    for (Flight flight : airlineCompany.getFlights()) {
                        if (flight.getDepartureAirport().equals(departureAirport)
                                && flight.getArrivalAirport().equals(arrivalAirport)) {
                            result.append("Flight Number: ").append(flight.getFlightNumber())
                                    .append(" - Departure: ").append(flight.getDepartureTime())
                                    .append(" from ").append(flight.getDepartureAirport())
                                    .append(", Arrival: ").append(flight.getArrivalTime())
                                    .append(" at ").append(flight.getArrivalAirport()).append("\n");
                            found = true;
                        }
                    }
                }
                if (!found) {
                    result.append("No flights found.");
                }
                return result.toString();
            }

            @Override
            public String getDescription() {
                return "search <departureAirport> <arrivalAirport> - Search for flights by departure and arrival airports";
            }
        });

        // 注册“book”命令，用于预约航班
        commands.put("book", new Command() {
            @Override
            public String execute(String... args) {
                if (args.length < 2) {
                    return "Usage: book <passengerId> <flightNumber>";
                }
                String passengerId = args[0];
                String flightNumber = args[1];
                Passenger passenger = Storage.getInstance().getPassenger(passengerId);
                Flight flight = Storage.getInstance().getFlight(flightNumber);
                if (flight == null) {
                    return "Flight not found";
                }
                if (passenger == null) {
                    return "Passenger not found";
                }
                if (!flight.bookTicket(passenger)) {
                    return "Flight is fully booked or passenger already booked";
                }

                passenger.makeReservation(flight, TicketCategory.ECONOMY, new ArrayList<>());

                return "Flight booked successfully";
            }

            @Override
            public String getDescription() {
                return "book <passengerId> <flightNumber> - Book a flight by passenger ID and flight number";
            }
        });

        // 注册“cancel”命令，用于取消航班预约
        commands.put("cancel", new Command() {
            @Override
            public String execute(String... args) {
                if (args.length < 2) {
                    return "Usage: cancel <passengerId> <flightNumber>";
                }
                String passengerId = args[0];
                String flightNumber = args[1];
                Passenger passenger = Storage.getInstance().getPassenger(passengerId);
                Flight flight = Storage.getInstance().getFlight(flightNumber);
                if (flight == null) {
                    return "Flight not found";
                }
                if (passenger == null) {
                    return "Passenger not found";
                }
                if (!flight.cancelTicket(passenger)) {
                    return "Passenger was not booked on this flight";
                }

                passenger.cancelReservationByFlight(flight);

                return "Flight cancelled successfully";
            }

            @Override
            public String getDescription() {
                return "cancel <passengerId> <flightNumber> - Cancel a booked flight by passenger ID and flight number";
            }
        });

        // 注册“book-service”命令，用于预订增值服务
        commands.put("book-service", new Command() {
            @Override
            public String execute(String... args) {
                if (args.length < 2) {
                    return "Usage: book-service <flightNumber> <serviceName>";
                }
                String flightNumber = args[0];
                String serviceName = args[1];
                Flight flight = Storage.getInstance().getFlight(flightNumber);
                AddOnService service = Storage.getInstance().getAddon(serviceName);
                if (flight == null) {
                    return "Flight not found";
                }
                if (service == null) {
                    return "Service not found";
                }
                if (!flight.bookService(service)) {
                    return "Service cannot be booked";
                }
                return "Service booked successfully";
            }

            @Override
            public String getDescription() {
                return "book-service <flightNumber> <serviceName> - Book an additional service for a flight";
            }
        });

        commands.put("my-flights", new Command() {
            @Override
            public String execute(String... args) {
                if (args.length < 1) {
                    return "Usage: my-flights <passengerId>";
                }
                String passengerId = args[0];
                Passenger passenger = Storage.getInstance().getPassenger(passengerId);
                if (passenger == null) {
                    return "Passenger not found";
                }
                var reservations = passenger.getReservations();

                StringBuilder result = new StringBuilder("Flights booked by passenger " + passengerId + ":\n");

                boolean found = false;
                for (Reservation reservation : reservations) {
                    result.append("Flight Number: ").append(reservation.getFlight().getFlightNumber())
                            .append(" - Departure: ").append(reservation.getFlight().getDepartureTime())
                            .append(" from ").append(reservation.getFlight().getDepartureAirport())
                            .append(", Arrival: ").append(reservation.getFlight().getArrivalTime())
                            .append(" at ").append(reservation.getFlight().getArrivalAirport())
                            .append(", Ticket Category: ").append(reservation.getTicketCategory())
                            .append(", Add-Ons: ").append(reservation.getAddOns())
                            .append("\n");
                    found = true;
                }

                if (!found) {
                    result.append("No flights found.");
                }
                return result.toString();
            }

            @Override
            public String getDescription() {
                return "my-flights <passengerId> - List all flights booked by a passenger";
            }
        });
    }

    @Override
    public String getName() {
        return "passenger";
    }

    @Override
    public String handleCommand(String input) {
        String[] parts = input.split("\\s+");
        String cmd = parts[0];
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        Command command = commands.get(cmd);
        if (command != null) {
            try {
                return command.execute(args);
            } catch (Exception e) {
                return e.toString();
            }
        }
        return "Unknown passenger command. Type 'help passenger' for available commands.";
    }

    @Override
    public String getHelp() {
        StringBuilder help = new StringBuilder("Passenger Menu Commands:\n");
        commands.values().forEach(cmd -> help.append(cmd.getDescription()).append("\n"));
        return help.toString();
    }
}