package com.horizon.flight.cmd.menus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.horizon.flight.cmd.Storage;
import com.horizon.flight.entities.AirlineCompany;
import com.horizon.flight.entities.Passenger;

public class ManageMenu implements Menu {
    private final Map<String, Command> commands = new HashMap<>();

    public ManageMenu() {
        // Passenger management commands
        commands.put("add-passenger", new Command() {
            @Override
            public String execute(String... args) {
                if (args.length < 2) {
                    return "Usage: add-passenger <id> <name>";
                }
                String id = args[0];
                String name = args[1];
                Passenger passenger = new Passenger(name);
                Storage.getInstance().addPassenger(id, passenger);
                return "Passenger added successfully.";
            }

            @Override
            public String getDescription() {
                return "add-passenger <id> <name> - Add a new passenger";
            }
        });

        commands.put("remove-passenger", new Command() {
            @Override
            public String execute(String... args) {
                if (args.length < 1) {
                    return "Usage: remove-passenger <id>";
                }
                String id = args[0];
                Storage.getInstance().getPassengers().remove(id);
                return "Passenger removed successfully.";
            }

            @Override
            public String getDescription() {
                return "remove-passenger <id> - Remove a passenger";
            }
        });

        commands.put("list-passengers", new Command() {
            @Override
            public String execute(String... args) {
                StringBuilder result = new StringBuilder("Passengers:\n");
                Storage.getInstance().getPassengers().forEach(
                        (id, passenger) -> result.append(id).append(" - ").append(passenger.getName()).append("\n"));
                return result.toString();
            }

            @Override
            public String getDescription() {
                return "list-passengers - List all passengers";
            }
        });

        commands.put("find-passenger", new Command() {
            @Override
            public String execute(String... args) {
                if (args.length < 1) {
                    return "Usage: find-passenger <name>";
                }
                String name = args[0];
                StringBuilder result = new StringBuilder("Found passengers:\n");
                boolean found = false;
                for (Map.Entry<String, Passenger> entry : Storage.getInstance().getPassengers().entrySet()) {
                    if (entry.getValue().getName().equals(name)) {
                        result.append(entry.getKey()).append(" - ").append(name).append("\n");
                        found = true;
                    }
                }
                return found ? result.toString() : "No passengers found with name: " + name;
            }

            @Override
            public String getDescription() {
                return "find-passenger <name> - Find passengers by name";
            }
        });

        // Airline company management commands
        commands.put("add-airline", new Command() {
            @Override
            public String execute(String... args) {
                if (args.length < 2) {
                    return "Usage: add-airline <id> <name>";
                }
                String id = args[0];
                String name = args[1];
                AirlineCompany airline = new AirlineCompany(name);
                Storage.getInstance().addAirlineCompany(id, airline);
                return "Airline company added successfully.";
            }

            @Override
            public String getDescription() {
                return "add-airline <id> <name> - Add a new airline company";
            }
        });

        commands.put("remove-airline", new Command() {
            @Override
            public String execute(String... args) {
                if (args.length < 1) {
                    return "Usage: remove-airline <id>";
                }
                String id = args[0];
                Storage.getInstance().getAllAirlineCompanies().remove(id);
                return "Airline company removed successfully.";
            }

            @Override
            public String getDescription() {
                return "remove-airline <id> - Remove an airline company";
            }
        });

        commands.put("list-airlines", new Command() {
            @Override
            public String execute(String... args) {
                StringBuilder result = new StringBuilder("Airline Companies:\n");
                Storage.getInstance().getAllAirlineCompanies().forEach(
                        (id, airline) -> result.append(id).append(" - ").append(airline.getName()).append("\n"));
                return result.toString();
            }

            @Override
            public String getDescription() {
                return "list-airlines - List all airline companies";
            }
        });

        commands.put("find-airline", new Command() {
            @Override
            public String execute(String... args) {
                if (args.length < 1) {
                    return "Usage: find-airline <name>";
                }
                String name = args[0];
                StringBuilder result = new StringBuilder("Found airlines:\n");
                boolean found = false;
                for (Map.Entry<String, AirlineCompany> entry : Storage.getInstance().getAllAirlineCompanies()
                        .entrySet()) {
                    if (entry.getValue().getName().equals(name)) {
                        result.append(entry.getKey()).append(" - ").append(name).append("\n");
                        found = true;
                    }
                }
                return found ? result.toString() : "No airline companies found with name: " + name;
            }

            @Override
            public String getDescription() {
                return "find-airline <name> - Find airline companies by name";
            }
        });
    }

    @Override
    public String getName() {
        return "manage";
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
        return "Unknown management command. Type 'help manage' for available commands.";
    }

    @Override
    public String getHelp() {
        StringBuilder help = new StringBuilder("Management Menu Commands:\n");
        commands.values().forEach(cmd -> help.append(cmd.getDescription()).append("\n"));
        return help.toString();
    }
}
