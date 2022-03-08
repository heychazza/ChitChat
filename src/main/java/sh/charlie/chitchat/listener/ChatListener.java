package sh.charlie.chitchat.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import sh.charlie.chitchat.ChitChatPlugin;
import sh.charlie.chitchat.util.MessageHandler;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ChatListener implements Listener {

    private final ChitChatPlugin plugin;

    public ChatListener(ChitChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onChat(AsyncChatEvent e) {
        Player player = e.getPlayer();
        e.setCancelled(true);

        List<String> formats = plugin.getFormats().stream().sorted(Comparator.comparing(item -> plugin.getConfig().getInt("formats." + item + ".priority"))).toList();

        for (String format : formats) {
            if (!format.equals("default") && !player.hasPermission("chatformat." + format)) continue;

            StringBuilder miniStr = new StringBuilder();

            List<String> configKeys = Arrays.asList("channel", "prefix", "name", "suffix", "chat");

            for (String configKey : configKeys) {
                String text = plugin.getConfig().getString("formats." + format + "." + configKey);
                String color = plugin.getConfig().getString("formats." + format + "." + configKey + "_color", "");
                List<String> textTooltip = plugin.getConfig().getStringList("formats." + format + "." + configKey + "_tooltip");
                String textClickCommand = plugin.getConfig().getString("formats." + format + "." + configKey + "_click_command");

                if (textTooltip.size() > 0) {
                    miniStr.append("<hover:show_text:\"").append(String.join("\n", textTooltip)).append("\">");
                }

                if (textClickCommand != null && !textClickCommand.isEmpty()) {
                    miniStr.append("<click:suggest_command:").append(PlaceholderAPI.setPlaceholders(player, textClickCommand)).append(">");
                }

                miniStr.append(color);
                miniStr.append(text);

                if (textClickCommand != null && !textClickCommand.isEmpty()) {
                    miniStr.append("</click>");
                }

                if (textTooltip.size() > 0) {
                    miniStr.append("</hover>");
                }
            }

            MiniMessage miniMessage = plugin.getMiniMessage();
            String chatColor = MessageHandler.replaceLegacyCodes(plugin.getConfig()
                    .getString("formats." + format + ".chat_color", ""));

            String legacyReplaced = MessageHandler.replaceLegacyCodes(miniStr.toString());
            String finalReplaced = legacyReplaced.replace("%message%",
                    chatColor + plugin.getMiniMessage().serialize(e.message()));
            Component component = miniMessage.deserialize(finalReplaced);

            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                component = MessageHandler.replacePlaceholderApiPlaceholders(player, component);
            }

            for (Audience audience : e.viewers()) {
                audience.sendMessage(component);
            }

            break;
        }
    }
}
