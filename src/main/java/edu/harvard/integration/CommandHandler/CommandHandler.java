package edu.harvard.integration.CommandHandler;

import edu.harvard.integration.Integrator;
import edu.harvard.integration.api.Cmd;
import org.reflections.Reflections;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handles all the commands that {@link Integrator} implements.
 */
public final class CommandHandler {
    private final List<Cmd> cmds = new ArrayList<>();

    public CommandHandler(String path) {
        Reflections reflections = new Reflections(path);
        Set<Class<? extends Cmd>> subTypes = reflections.getSubTypesOf(Cmd.class);
        subTypes.forEach(c -> loadCommand(c.getSimpleName(), path + '.'));
    }

    private void loadCommand(@Nonnull String name, @Nonnull String pkg) {
        try {
            Cmd command = (Cmd) Cmd.class.getClassLoader().loadClass(pkg + name).newInstance();
            if (command != null)
                this.cmds.add(command);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ignored) {
        }
    }

    /**
     * Handles a message sent by {@link CommandSender}, and if it is a {@link Cmd} attempts to run it.
     * @param message The message that contains the command and any arguments.
     * @param sender  The {@link CommandSender} trying to perform a {@link Cmd}.
     * @return True if a {@link Cmd} was found and successfully run, false if no {@link Cmd} was found or the sender does not have permission to run the {@link Cmd}.
     */
    public boolean handleCommand(@Nonnull String message, @Nonnull CommandSender sender) {//TODO: Log when commands are performed, maybe only to a log file
        message = message.trim().replaceAll("\\s\\s+", " ");//Replace all multiple spaces with a single space
        if (message.startsWith("!"))
            message = message.replaceFirst("!", "");
        String command = message.split(" ")[0];
        String arguments = message.replaceFirst(command, "").trim();
        String[] args = arguments.equals("") ? new String[0] : arguments.split(" ");
        CommandSource source = sender.getSource();
        for (Cmd cmd : this.cmds) {
            if (cmd.getName().equalsIgnoreCase(command) || cmd.getAliases() != null && cmd.getAliases().contains(command.toLowerCase())) {
                List<CommandSource> sources = cmd.supportedSources();
                if (sources != null && !sources.contains(source)) {
                    StringBuilder validSources = new StringBuilder();
                    for (int i = 0; i < sources.size(); i++) {
                        if (!validSources.toString().equals("")) {
                            validSources.append(i == 2 && i == sources.size() ? " " : ", ");
                            if (i == sources.size())
                                validSources.append("or ");
                        }
                        validSources.append(sources.get(i));
                    }
                    sender.sendMessage("Error: This command must be used through " + validSources);
                    return true;
                }
                cmd.performCommand(args, sender);
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the help list for all the commands that {@link CommandSender} can perform.
     * @param sender The {@link CommandSender} to retrieve the help list for.
     * @return The complete help list of commands that {@link CommandSender} can perform.
     */
    @Nonnull
    public List<String> getHelpList(@Nonnull CommandSender sender) {
        List<String> help = new ArrayList<>();
        this.cmds.stream().forEachOrdered(cmd -> {
            List<CommandSource> sources = cmd.supportedSources();
            if (sources == null || sources.contains(sender.getSource()))
                help.add(cmd.getUsage() + " ~ " + cmd.helpDoc());
        });
        return help;
    }
}