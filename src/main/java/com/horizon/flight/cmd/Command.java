package com.horizon.flight.cmd;

public interface Command {
    String execute(String... args);

    String getDescription();
}

// Menu interface for all menu handlers