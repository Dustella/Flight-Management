package com.horizon.flight.cmd.menus;

public interface Command {
    String execute(String... args) throws Exception;

    String getDescription();
}

// Menu interface for all menu handlers