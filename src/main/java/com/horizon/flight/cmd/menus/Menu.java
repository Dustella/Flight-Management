package com.horizon.flight.cmd.menus;

public interface Menu {
    String getName();

    String handleCommand(String input);

    String getHelp();
}