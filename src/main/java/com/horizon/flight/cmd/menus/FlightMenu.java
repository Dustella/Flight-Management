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

        // 创建航班
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
                    String airlineCompanyName = args[6];

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
                return "create <flightNumber> <departureTime> <arrivalTime> <departureAirport> <arrivalAirport> <capacity> <airlineCompany> - Create a new flight";
            }
        });

        // 列出所有航班
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

        // 删除航班
        commands.put("cancel", new Command() {
            @Override
            public String execute(String... args) throws Exception {
                if (args.length < 2) {
                    return "Usage: delete <airlineCompany> <flightNumber>";
                }
                String airlineCompanyName = args[0];
                String flightNumber = args[1];

                AirlineCompany airlineCompany = Storage.getInstance().getAirlineCompany(airlineCompanyName);
                if (airlineCompany == null) {
                    return "No airline company found with name: " + airlineCompanyName;
                }

                boolean removed = airlineCompany.cancelFlightByNumber(flightNumber);
                if (removed) {
                    return "Flight " + flightNumber + " canceled successfully.";
                } else {
                    return "Flight " + flightNumber + " not found.";
                }
            }

            @Override
            public String getDescription() {
                return "cancel <airlineCompany> <flightNumber> - Cancel a flight";
            }
        });

        // 修改航班
        commands.put("update", new Command() {
            @Override
            public String execute(String... args) throws Exception {
                if (args.length < 7) {
                    return "Usage: update <airlineCompany> <flightNumber> <departureTime> <arrivalTime> <departureAirport> <arrivalAirport> <capacity>";
                }
                String airlineCompanyName = args[0];
                String flightNumber = args[1];
                LocalDateTime departureTime = LocalDateTime.parse(args[2]);
                LocalDateTime arrivalTime = LocalDateTime.parse(args[3]);

                AirlineCompany airlineCompany = Storage.getInstance().getAirlineCompany(airlineCompanyName);
                if (airlineCompany == null) {
                    return "No airline company found with name: " + airlineCompanyName;
                }

                Flight flight = airlineCompany.findFlightByNumber(flightNumber);
                if (flight == null) {
                    return "Flight " + flightNumber + " not found.";
                }

                flight.setDepartureTime(departureTime);
                flight.setArrivalTime(arrivalTime);
                return "Flight " + flightNumber + " updated successfully.";
            }

            @Override
            public String getDescription() {
                return "update <airlineCompany> <flightNumber> <departureTime> <arrivalTime> <departureAirport> <arrivalAirport> <capacity> - Update flight information";
            }
        });

        // 查询航班
        commands.put("find", new Command() {
            @Override
            public String execute(String... args) throws Exception {
                if (args.length < 2) {
                    return "Usage: find <airlineCompany> <flightNumber>";
                }
                String airlineCompanyName = args[0];
                String flightNumber = args[1];

                AirlineCompany airlineCompany = Storage.getInstance().getAirlineCompany(airlineCompanyName);
                if (airlineCompany == null) {
                    return "No airline company found with name: " + airlineCompanyName;
                }

                Flight flight = airlineCompany.findFlightByNumber(flightNumber);
                if (flight == null) {
                    return "Flight " + flightNumber + " not found.";
                }

                return flight.toString();
            }

            @Override
            public String getDescription() {
                return "find <airlineCompany> <flightNumber> - Find flight details by flight number";
            }
        });

        // 航班延误
        commands.put("delay", new Command() {
            @Override
            public String execute(String... args) throws Exception {
                if (args.length < 3) {
                    return "Usage: delay <airlineCompany> <flightNumber> <delayMinutes>";
                }
                String airlineCompanyName = args[0];
                String flightNumber = args[1];
                int delayMinutes;

                // 验证 <delayMinutes> 是否为有效的整数
                try {
                    delayMinutes = Integer.parseInt(args[2]);
                    if (delayMinutes < 0) {
                        return "Error: <delayMinutes> must be a non-negative integer.";
                    }
                } catch (NumberFormatException e) {
                    return "Error: <delayMinutes> must be a valid integer.";
                }

                AirlineCompany airlineCompany = Storage.getInstance().getAirlineCompany(airlineCompanyName);
                if (airlineCompany == null) {
                    return "No airline company found with name: " + airlineCompanyName;
                }

                Flight flight = airlineCompany.findFlightByNumber(flightNumber);
                if (flight == null) {
                    return "Flight " + flightNumber + " not found.";
                }

                flight.setDepartureTime(flight.getDepartureTime().plusMinutes(delayMinutes));
                flight.setArrivalTime(flight.getArrivalTime().plusMinutes(delayMinutes));

                return "Flight " + flightNumber + " delayed by " + delayMinutes + " minutes.";
            }

            @Override
            public String getDescription() {
                return "delay <airlineCompany> <flightNumber> <delayMinutes> - Delay a flight by a specified number of minutes";
            }
        });

        // 按航空公司查询航班
        commands.put("searchByAirline", new Command() {
            @Override
            public String execute(String... args) throws Exception {
                if (args.length < 1) {
                    return "Usage: searchByAirline <airlineCompany>";
                }
                String airlineCompanyName = args[0];

                AirlineCompany airlineCompany = Storage.getInstance().getAirlineCompany(airlineCompanyName);
                if (airlineCompany == null) {
                    return "No airline company found with name: " + airlineCompanyName;
                }

                StringBuilder result = new StringBuilder("Flights for airline: ").append(airlineCompanyName).append("\n");
                for (Flight flight : airlineCompany.getFlights()) {
                    result.append(flight.getFlightNumber()).append(" - ")
                            .append(flight.getDepartureAirport()).append(" to ")
                            .append(flight.getArrivalAirport()).append(" (")
                            .append(flight.getDepartureTime()).append(" to ")
                            .append(flight.getArrivalTime()).append(")\n");
                }
                return result.toString();
            }

            @Override
            public String getDescription() {
                return "searchByAirline <airlineCompany> - Search flights by airline company";
            }
        });

        // 按出发地和目的地查询航班
        commands.put("searchByRoute", new Command() {
            @Override
            public String execute(String... args) throws Exception {
                if (args.length < 2) {
                    return "Usage: searchByRoute <departureAirport> <arrivalAirport>";
                }
                String departureAirport = args[0];
                String arrivalAirport = args[1];

                StringBuilder result = new StringBuilder("Flights from ").append(departureAirport)
                        .append(" to ").append(arrivalAirport).append(":\n");

                var allAirlineCompanies = Storage.getInstance().getAllAirlineCompanies();
                for (var airlineCompany : allAirlineCompanies.values()) {
                    for (Flight flight : airlineCompany.getFlights()) {
                        if (flight.getDepartureAirport().equalsIgnoreCase(departureAirport) &&
                                flight.getArrivalAirport().equalsIgnoreCase(arrivalAirport)) {
                            result.append(flight.getFlightNumber()).append(" - ")
                                    .append(airlineCompany.getName()).append(" (")
                                    .append(flight.getDepartureTime()).append(" to ")
                                    .append(flight.getArrivalTime()).append(")\n");
                        }
                    }
                }
                return result.toString();
            }

            @Override
            public String getDescription() {
                return "searchByRoute <departureAirport> <arrivalAirport> - Search flights by departure and arrival location";
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