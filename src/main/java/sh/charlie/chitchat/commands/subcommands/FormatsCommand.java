package sh.charlie.chitchat.commands.subcommands;

import org.bukkit.entity.Player;
import sh.charlie.chitchat.ChitChatPlugin;
import sh.charlie.chitchat.commands.SubCommand;

public class FormatsCommand extends SubCommand {

    public FormatsCommand(ChitChatPlugin plugin) {
        super(plugin, "formats", "chitchat.formats");
    }

    @Override
    public void execute(Player player, String[] args) {
        player.sendMessage(plugin.parse("&7"));
        player.sendMessage(plugin.parse("&b[ChitChat] &7Formats (" + plugin.getFormats().size() + "):"));
        player.sendMessage(plugin.parse("&7"));
        for (String format : plugin.getFormats()) {
            player.sendMessage(plugin.parse(" &7- &b" + format + " &f(chatformat." + format + ")"));
        }
        player.sendMessage(plugin.parse("&7"));
    }

}
