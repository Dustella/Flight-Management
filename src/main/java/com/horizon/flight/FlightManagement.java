
package com.horizon.flight;

import com.horizon.flight.cmd.CLIClient;
import com.horizon.flight.cmd.CLIServer;

public class FlightManagement {

    public static void main(String[] args) {
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
        } catch (Exception e) {
            System.err.println("Could not initialize server: " + e.getMessage());
            System.exit(1);
        }

        CLIClient client = new CLIClient("localhost", 1234);
        client.start();

    }
}
