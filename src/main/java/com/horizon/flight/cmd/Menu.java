package com.horizon.flight.cmd;

public interface Menu {
    String getName();

    String handleCommand(String input);

    String getHelp();
}