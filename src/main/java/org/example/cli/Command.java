package org.example.cli;

public interface Command {
    void execute();

    String getCommandName();
}
