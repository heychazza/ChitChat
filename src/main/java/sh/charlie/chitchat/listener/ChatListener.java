package sh.charlie.chitchat.listener;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import sh.charlie.chitchat.ChitChatPlugin;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ChatListener implements Listener {

    private final ChitChatPlugin plugin;

    public ChatListener(ChitChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        e.setCancelled(true);

        List<String> formats = plugin.getFormats().stream().sorted(Comparator.comparing(item -> plugin.getConfig().getInt("formats." + item + ".priority"))).toList();

        for (String format : formats) {
            if(!format.equals("default") && !player.hasPermission("chatformat." + format)) continue;

            StringBuilder miniStr = new StringBuilder();

            List<String> configKeys = Arrays.asList("channel", "name", "prefix", "suffix", "chat");

            for (String configKey : configKeys) {
                String text = plugin.getConfig().getString("formats." + format + "." + configKey);
                List<String> textTooltip = plugin.getConfig().getStringList("formats." + format + "." + configKey + "_tooltip");
                String textClickCommand = plugin.getConfig().getString("formats." + format + "." + configKey + "_click_command");

                if(textTooltip.size() > 0) {
                    miniStr.append("<hover:show_text:\"").append(String.join("\n", textTooltip)).append("\">");
                }

                if(textClickCommand != null && !textClickCommand.isEmpty()) {
                    miniStr.append("<click:suggest_command:").append(textClickCommand).append(">");
                }

                miniStr.append(text);

                if(textTooltip.size() > 0) {
                    miniStr.append("</hover>");
                }

                if(textClickCommand != null && !textClickCommand.isEmpty()) {
                    miniStr.append("</click>");
                }
            }

            MiniMessage miniMessage = plugin.getMiniMessage();
            miniStr = new StringBuilder(miniStr.toString().replace("%message%", miniMessage.escapeTokens(e.getMessage())));

            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                miniStr = new StringBuilder(PlaceholderAPI.setPlaceholders(e.getPlayer(), miniStr.toString()));
            }

            plugin.getAdventure().player(e.getPlayer()).sendMessage(miniMessage.parse(miniStr.toString()));
            break;
        }
    }
}
