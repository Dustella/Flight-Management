package com.horizon.flight.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.horizon.flight.cmd.menus.FlightMenu;
import com.horizon.flight.cmd.menus.Menu;

public class CLIServer {
    private final Map<String, Menu> menus = new HashMap<>();
    private final ServerSocket serverSocket;
    private final ExecutorService threadPool;
    private static final int MAX_CLIENTS = 10;

    public CLIServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        threadPool = Executors.newFixedThreadPool(MAX_CLIENTS);
        initializeMenus();
    }

    private void initializeMenus() {
        var dbMenu = new FlightMenu();
        menus.put(dbMenu.getName(), (Menu) dbMenu);
    }

    public void start() {
        System.out.println("Server listening on port " + serverSocket.getLocalPort());

        while (!serverSocket.isClosed()) {
            try {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(new ClientHandler(clientSocket, menus));
            } catch (IOException e) {
                System.err.println("Error accepting client connection: " + e.getMessage());
            }
        }
    }

    public void stop() {
        try {
            serverSocket.close();
            threadPool.shutdown();
        } catch (IOException e) {
            System.err.println("Error stopping server: " + e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private final Map<String, Menu> menus;

        public ClientHandler(Socket socket, Map<String, Menu> menus) {
            this.clientSocket = socket;
            this.menus = menus;
        }

        @Override
        public void run() {
            try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                handleClientConnection(out, in);

            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        }

        private void handleClientConnection(PrintWriter out, BufferedReader in) throws IOException {
            out.println("Welcome to Custom CLI. Type 'help' for commands, 'exit' to quit.");

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                inputLine = inputLine.trim();

                if (inputLine.equalsIgnoreCase("exit")) {
                    out.println("Goodbye!");
                    break;
                }

                if (inputLine.equalsIgnoreCase("help")) {
                    printGeneralHelp(out);
                    continue;
                }

                if (inputLine.startsWith("help ")) {
                    String menuName = inputLine.substring(5).trim();
                    Menu menu = menus.get(menuName);
                    if (menu != null) {
                        out.println(menu.getHelp());
                    } else {
                        out.println("Unknown menu: " + menuName);
                    }
                    continue;
                }

                // Handle menu selection or command execution
                String[] parts = inputLine.split("\\s+", 2);
                String menuName = parts[0];

                Menu menu = menus.get(menuName);
                if (menu != null) {
                    String command = parts.length > 1 ? parts[1] : "";
                    String response = menu.handleCommand(command);
                    out.println(response);
                } else {
                    out.println("Unknown command or menu. Type 'help' for available commands.");
                }
            }
        }

        private void printGeneralHelp(PrintWriter out) {
            StringBuilder help = new StringBuilder("Available menus:\n");
            menus.keySet().forEach(menuName -> help.append(menuName).append(" - Type 'help ").append(menuName)
                    .append("' for specific commands\n"));
            help.append("\nGeneral commands:\n");
            help.append("help - Show this help\n");
            help.append("help <menu> - Show help for specific menu\n");
            help.append("exit - Close connection");
            out.println(help.toString());
        }
    }
}