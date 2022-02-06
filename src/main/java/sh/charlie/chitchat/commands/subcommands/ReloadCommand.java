package sh.charlie.chitchat.commands.subcommands;

import org.bukkit.entity.Player;
import sh.charlie.chitchat.ChitChatPlugin;
import sh.charlie.chitchat.commands.SubCommand;

public class ReloadCommand extends SubCommand {

    public ReloadCommand(ChitChatPlugin plugin) {
        super(plugin, "reload", "chitchat.reload");
    }

    @Override
    public void execute(Player player, String[] args) {
        plugin.reloadConfig();
        plugin.setFormats();

        player.sendMessage(plugin.parse("&b[ChitChat] &7Configuration reloaded."));
    }

}
