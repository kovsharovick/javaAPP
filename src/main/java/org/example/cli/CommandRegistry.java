package org.example.cli;

import org.example.model.User;

import java.util.*;

public class CommandRegistry {
    private final Map<String, Command> commands = new HashMap<>();

    public void register(Command cmd) {
        commands.put(cmd.getName().toLowerCase(), cmd);
    }

    public Optional<Command> find(String name) {
        return Optional.ofNullable(commands.get(name.toLowerCase()));
    }

    public CommandLookup lookup(String[] parts) {
        if (parts.length == 0) return new CommandLookup("", "");
        if (parts.length == 1) {
            return new CommandLookup(parts[0], parts[0]);
        }

        String twoWordKey = parts[0] + " " + parts[1];
        if (commands.containsKey(twoWordKey)) {
            return new CommandLookup(twoWordKey, twoWordKey);
        }
        return new CommandLookup(parts[0], parts[0]);
    }

    public boolean canExecute(Command cmd, User currentUser) {
        if (!cmd.isAdminOnly()) return true;
        return currentUser != null && currentUser.getAdmin();
    }

    public Collection<Command> getAllCommands() {
        return commands.values();
    }
}