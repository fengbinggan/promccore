package mc.promcteam.engine.commands;

import mc.promcteam.engine.NexPlugin;
import mc.promcteam.engine.commands.api.IGeneralCommand;
import mc.promcteam.engine.commands.list.*;
import mc.promcteam.engine.manager.api.Loadable;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class CommandManager<P extends NexPlugin<P>> implements Loadable {

    @NotNull
    private P                       plugin;
    private Set<IGeneralCommand<P>> commands;
    private MainCommand<P>          mainCommand;

    public CommandManager(@NotNull P plugin) {
        this.plugin = plugin;
    }

    @Override
    public void setup() {
        this.commands = new HashSet<>();

        // Create main plugin command and attach help sub-command as a default executor.
        this.mainCommand = new MainCommand<P>(this.plugin);
        this.mainCommand.addDefaultCommand(new HelpCommand<P>(this.plugin));

        // Register child plugin sub-commands to the main plugin command.
        this.plugin.registerCmds(this.mainCommand);

        // Check for plugin settings to register default commands.
        if (this.plugin.hasEditor()) {
            this.mainCommand.addSubCommand(new EditorCommand<P>(this.plugin));
        }
        this.mainCommand.addSubCommand(new ReloadCommand<P>(this.plugin));

        if (!this.plugin.isEngine()) {
            this.mainCommand.addSubCommand(new AboutCommand<P>(this.plugin));
        }

        // Register main command as a bukkit command.
        this.registerCommand(this.mainCommand);
    }

    @Override
    public void shutdown() {
        for (IGeneralCommand<P> cmd : new HashSet<>(this.commands)) {
            this.unregisterCommand(cmd);
            cmd.clearSubCommands();
            cmd = null;
        }
        this.commands.clear();
    }

    @NotNull
    public Set<IGeneralCommand<P>> getCommands() {
        return this.commands;
    }

    @NotNull
    public MainCommand<P> getMainCommand() {
        return this.mainCommand;
    }

    public void registerCommand(@NotNull IGeneralCommand<P> cmd) {
        if (this.commands.add(cmd)) {
            CommandRegister.register(this.plugin, cmd);
        }
    }

    public void unregisterCommand(@NotNull IGeneralCommand<P> cmd) {
        if (this.commands.remove(cmd)) {
            CommandRegister.unregister(this.plugin, cmd.labels());
        }
    }
}
