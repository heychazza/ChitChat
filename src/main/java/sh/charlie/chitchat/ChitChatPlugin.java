package sh.charlie.chitchat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import sh.charlie.chitchat.commands.AnalyseCommand;
import sh.charlie.chitchat.listener.ChatListener;

public class ChitChatPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getCommand("chitchat").setExecutor(new AnalyseCommand(this));
        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);
    }

    public String parse(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
