package com.horizon.flight.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

            readServerResponse(in);

            while (true) {
                String fromUser = stdIn.readLine();
                if (fromUser == null || "exit".equalsIgnoreCase(fromUser))
                    break;

                out.println(fromUser);
                readServerResponse(in);
            }
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }

    private void readServerResponse(BufferedReader in) throws IOException {
        StringBuilder response = new StringBuilder();
        char[] buffer = new char[1024];

        waitForData(in);

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

    private void waitForData(BufferedReader in) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        try {
            while (!in.ready()) {
                scheduler.schedule(() -> {
                }, 50, TimeUnit.MILLISECONDS).get();
            }
        } catch (InterruptedException | ExecutionException | IOException e) {
            Thread.currentThread().interrupt();
        } finally {
            scheduler.shutdown();
        }
    }
}