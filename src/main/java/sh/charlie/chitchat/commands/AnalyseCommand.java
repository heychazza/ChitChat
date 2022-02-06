package sh.charlie.chitchat.commands;

import sh.charlie.chitchat.ChitChatPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sh.charlie.chitchat.commands.subcommands.ReloadCommand;
import java.util.*;

public class AnalyseCommand implements CommandExecutor {

    private final ChitChatPlugin plugin;
    private final Map<String, SubCommand> commands = new HashMap<>();

    public AnalyseCommand(ChitChatPlugin plugin) {
        this.plugin = plugin;

        Arrays.asList(
                new ReloadCommand(plugin)
        ).forEach(command -> commands.put(command.getName(), command));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;

        if (args.length == 0 || !commands.containsKey(args[0].toLowerCase())) {
            sender.sendMessage(plugin.parse("&b[ChitChat] &7Running on &bv" + plugin.getDescription().getVersion() + "&7."));
            return true;
        }

        SubCommand subCommand = commands.get(args[0].toLowerCase());
        if (!sender.hasPermission(subCommand.getPermission())) {
            sender.sendMessage(plugin.parse("&b[ChitChat] &7You do not have access to that command."));
            return true;
        }

        subCommand.execute((Player) sender, Arrays.copyOfRange(args, 1, args.length));
        return true;
    }

}
