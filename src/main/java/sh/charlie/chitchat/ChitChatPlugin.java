package sh.charlie.chitchat;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import sh.charlie.chitchat.commands.ChitChatCommand;
import sh.charlie.chitchat.listener.ChatListener;

import java.util.Set;

public class ChitChatPlugin extends JavaPlugin implements Listener {

    private Set<String> formats;
    private MiniMessage miniMessage;
    private BukkitAudiences adventure;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getCommand("chitchat").setExecutor(new ChitChatCommand(this));
        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);

        setFormats();

        miniMessage = MiniMessage.miniMessage();
        adventure = BukkitAudiences.create(this);
    }

    public void setFormats() {
        formats = getConfig().getConfigurationSection("formats").getKeys(false);
    }

    public Set<String> getFormats() {
        return formats;
    }

    public MiniMessage getMiniMessage() {
        return miniMessage;
    }

    public BukkitAudiences getAdventure() {
        return adventure;
    }

    public String parse(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
