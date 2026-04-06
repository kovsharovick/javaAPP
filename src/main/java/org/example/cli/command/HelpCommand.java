package org.example.cli.command;

import org.example.cli.Command;
import org.example.cli.CommandContext;
import org.example.model.User;

import java.util.Collection;

public class HelpCommand implements Command {
    private final Collection<Command> allCommands;

    public HelpCommand(Collection<Command> allCommands) {
        this.allCommands = allCommands;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "help – показать справку";
    }

    @Override
    public boolean isAdminOnly() {
        return false;
    }

    @Override
    public void execute(CommandContext ctx, String[] args) {
        User user = ctx.getCurrentUser();
        boolean isAdmin = user != null && user.getAdmin();
        ctx.getOut().println("Доступные команды:");
        for (Command cmd : allCommands) {
            if (cmd.isAdminOnly() && !isAdmin) continue;
            ctx.getOut().println("  " + cmd.getDescription());
        }
    }
}