package com.horizon.flight.cmd;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class DatabaseMenu implements Menu {
    private final Map<String, Command> commands = new HashMap<>();

    public DatabaseMenu() {
        commands.put("list", new Command() {
            @Override
            public String execute(String... args) {
                return "Listing databases...";
            }

            @Override
            public String getDescription() {
                return "list - List all databases";
            }
        });

        commands.put("create", new Command() {
            @Override
            public String execute(String... args) {
                if (args.length < 1)
                    return "Usage: create <dbname>";
                return "Creating database " + args[0];
            }

            @Override
            public String getDescription() {
                return "create <dbname> - Create a new database";
            }
        });
    }

    @Override
    public String getName() {
        return "database";
    }

    @Override
    public String handleCommand(String input) {
        String[] parts = input.split("\\s+");
        String cmd = parts[0];
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        Command command = commands.get(cmd);
        if (command != null) {
            return command.execute(args);
        }
        return "Unknown database command. Type 'help database' for available commands.";
    }

    @Override
    public String getHelp() {
        StringBuilder help = new StringBuilder("Database Menu Commands:\n");
        commands.values().forEach(cmd -> help.append(cmd.getDescription()).append("\n"));
        return help.toString();
    }
}
