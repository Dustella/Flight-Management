package com.horizon.flight.cmd.menus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.horizon.flight.cmd.Storage;
import com.horizon.flight.entities.AirlineCompany;
import com.horizon.flight.entities.Flight;

public class FlightMenu implements Menu {
    private final Map<String, Command> commands = new HashMap<>();

    public FlightMenu() {

        commands.put("create", new Command() {
            @Override
            public String execute(String... args) throws Exception {
                if (args.length < 7) {
                    return "Usage: create <flightNumber> <departureTime> <arrivalTime> <departureAirport> <arrivalAirport> <capacity> <airlineCompany>";
                }
                try {
                    String flightNumber = args[0];
                    LocalDateTime departureTime = LocalDateTime.parse(args[1]);
                    LocalDateTime arrivalTime = LocalDateTime.parse(args[2]);
                    String departureAirport = args[3];
                    String arrivalAirport = args[4];
                    int capacity = Integer.parseInt(args[5]);
                    String airlineCompanyName = args[7];

                    AirlineCompany airlineCompany = Storage.getInstance().getAirlineCompany(airlineCompanyName);

                    if (airlineCompany == null) {
                        throw new Exception("No airline company found");
                    }

                    Flight flight = new Flight(flightNumber, departureTime, arrivalTime, departureAirport,
                            arrivalAirport, capacity);
                    airlineCompany.addFlight(flight);
                    return "Flight created successfully.";
                } catch (NumberFormatException e) {
                    return "Error creating flight: " + e.getMessage();
                }
            }

            @Override
            public String getDescription() {
                return "create <flightNumber> <departureTime> <arrivalTime> <departureAirport> <arrivalAirport> <capacity> - Create a new flight";
            }
        });

        commands.put("list", new Command() {
            @Override
            public String execute(String... args) {
                StringBuilder result = new StringBuilder("Flights:\n");
                var allAirlineCompanies = Storage.getInstance().getAllAirlineCompanies();
                for (var airlineCompany : allAirlineCompanies.values()) {
                    for (Flight flight : airlineCompany.getFlights()) {
                        result.append(flight.getFlightNumber()).append(" - ")
                                .append(flight.getDepartureAirport()).append(" to ")
                                .append(flight.getArrivalAirport()).append(" (")
                                .append(flight.getDepartureTime()).append(" to ")
                                .append(flight.getArrivalTime()).append(")\n");
                    }
                }
                return result.toString();
            }

            @Override
            public String getDescription() {
                return "list - List all flights";
            }
        });
    }

    @Override
    public String getName() {
        return "flight";
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
        return "Unknown flight command. Type 'help flight' for available commands.";
    }

    @Override
    public String getHelp() {
        StringBuilder help = new StringBuilder("Flight Menu Commands:\n");
        commands.values().forEach(cmd -> help.append(cmd.getDescription()).append("\n"));
        return help.toString();
    }
}