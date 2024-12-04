package com.horizon.flight.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CLIClient {
    private final String serverAddress;
    private final int serverPort;

    public CLIClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void start() {
        try (Socket socket = new Socket(serverAddress, serverPort);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            // Read initial server message
            readServerResponse(in);

            // Communication loop
            while (true) {
                // Read user input
                String fromUser = stdIn.readLine();
                if (fromUser == null)
                    break;

                out.println(fromUser);
                if ("exit".equalsIgnoreCase(fromUser))
                    break;

                // Read complete server response
                readServerResponse(in);
            }
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }

    private void readServerResponse(BufferedReader in) throws IOException {
        StringBuilder response = new StringBuilder();
        char[] buffer = new char[1024];

        // Wait briefly for data to arrive
        while (!in.ready()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        // Read all available data
        while (in.ready()) {
            int charsRead = in.read(buffer);
            if (charsRead == -1)
                break;
            response.append(buffer, 0, charsRead);
        }

        if (response.length() > 0) {
            System.out.print(response.toString());
        }
    }

}