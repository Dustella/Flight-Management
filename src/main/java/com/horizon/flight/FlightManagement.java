
package com.horizon.flight;

import java.io.File;
import java.io.IOException;

import com.horizon.flight.cmd.CLIClientJLine;
import com.horizon.flight.cmd.CLIServer;
import com.horizon.flight.cmd.Storage;

public class FlightManagement {

    public static void main(String[] args) {
        String mode = "full"; // default mode
        if (args.length > 0) {
            mode = args[0];
        }

        switch (mode) {
            case "--mode=client" -> {
                CLIClientJLine client = new CLIClientJLine("localhost", 1234);
                client.start();
                return;
            }
            case "--mode=server" -> {
                startServer();
                return;
            }
            case "--mode=full" -> {
            }
            default -> {
                if (args.length > 0) {
                    System.err.println("Invalid mode. Use --mode=client, --mode=server, or --mode=full.");
                    System.exit(1);
                }
            }
        }

        prepareStorage();
        startServer();
        // CLIClient client = new CLIClient("localhost", 1234);
        // client.start();
        CLIClientJLine client = new CLIClientJLine("localhost", 1234);
        client.start();

    }

    public static void prepareStorage() {
        // Initialize storage
        // find if there is storage.dump under dir
        File storageFile = new File("storage.dump");
        if (storageFile.exists()) {
            try {
                Storage.readStorageFromFile("storage.dump");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Could not read storage file: " + e.getMessage());
            }
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("Shutting down...");
            try {
                Storage.dumpStorageToFile("storage.dump");
            } catch (IOException e) {
                System.err.println("Could not write storage file: " + e.getMessage());
            }
        }));

    }

    public static void startServer() {
        try {
            CLIServer server = new CLIServer(1234);
            Thread serverThread = new Thread(() -> {
                try {
                    server.start();
                } catch (Exception e) {
                    System.err.println("Could not start server: " + e.getMessage());
                    System.exit(1);
                }
            });
            serverThread.start();
        } catch (IOException e) {
            System.err.println("Could not initialize server: " + e.getMessage());
            System.exit(1);
        }
    }
}
